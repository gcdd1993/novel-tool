package com.gaochen.novel.downloader.service;

import com.gaochen.novel.downloader.domain.NovelChapter;
import com.gaochen.novel.downloader.exception.CustomException;
import com.gaochen.novel.downloader.exception.ErrorMessage;
import com.gaochen.novel.downloader.util.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by IntelliJ IDEA.
 * User: gaochen
 * Date: 2018/8/21
 */
public class Downloader extends Thread {
    private static final ContentService CONTENT_SERVICE = ContentService.getInstance();

    private ReentrantLock nextLock = new ReentrantLock();
    private Condition nextCondition = nextLock.newCondition();

    private Queue<NovelChapter> novelChapterQueue = new LinkedBlockingQueue<>();
    private String novelName;
    private String parentPath;

    private AtomicInteger stat = new AtomicInteger(STAT_INIT);

    private final static int STAT_INIT = 0;

    private final static int STAT_RUNNING = 1;

    private final static int STAT_STOPPED = 2;

    private final AtomicLong pageCount = new AtomicLong(0);

    private CountableThreadPool threadPool;

    public Downloader(int thread, String novelName, String parentPath, List<NovelChapter> novelChapterList) {
        this.threadPool = new CountableThreadPool(thread);
        this.novelName = novelName;
        this.parentPath = parentPath;
        novelChapterQueue.addAll(novelChapterList);
    }

    @Override
    public void run() {
        stat.set(STAT_RUNNING);
        while (!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            final NovelChapter next = novelChapterQueue.poll();
            if(next == null) {
                if (threadPool.getThreadAlive() == 0) {
                    break;
                }
                waitNext();
            }else {
                threadPool.execute(() -> {
                    System.out.println("开始下载" + next);
                    try {
                        File realPath = new File(parentPath + "/" + novelName);
                        if(!realPath.exists()) {
                            realPath.mkdirs();
                        }
                        String text = CONTENT_SERVICE.text(next.getUrl());
                        if(text != null) {
                            FileWriter fileWriter = new FileWriter(new File(realPath,  FileUtils.specificatFileName(next.getName())+ ".txt"));
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(text);
                            bufferedWriter.flush();
                            bufferedWriter.close();
                            fileWriter.close();
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new CustomException(String.format(ErrorMessage.THREAD_ERROR,e.getMessage()));
                    } catch (IOException e) {
                        throw new CustomException(String.format(ErrorMessage.IO_ERROR,e.getMessage()));
                    } finally {
                        pageCount.incrementAndGet();
                        signalAll();
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
    }

    private void waitNext() {
        nextLock.lock();
        try {
            if(threadPool.getThreadAlive() == 0) {
                return;
            }
            nextCondition.await(30000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new CustomException(String.format(ErrorMessage.THREAD_ERROR,e.getMessage()));
        } finally {
            nextLock.unlock();
        }
    }

    private void signalAll() {
        try {
            nextLock.lock();
            nextCondition.signalAll();
        } finally {
            nextLock.unlock();
        }
    }

    public void stopAll() {
        stat.compareAndSet(STAT_RUNNING, STAT_STOPPED);
    }

    public int checkIfRunning() {
        return stat.get();
    }

    public long currentSuccess() {
        return pageCount.get();
    }

}
