package com.frameworkset.orm.annotation;

public enum TransactionType
    {
        /**
         * 始终创建新事务
         */
        NEW_TRANSACTION,
        
        /**
         * 如果没有事务创建新事务，有事务加入当前事务
         */
        REQUIRED_TRANSACTION,
        /**
         * 有事务就加入事务，没有不创建事务,默认情况
         */
        MAYBE_TRANSACTION,
        /**
         * 没有事务
         */
        NO_TRANSACTION,
        
        /**
         * 未知事务类型
         */
        UNKNOWN_TRANSACTION,
        
        /**
         * 读写事务类型，支持数据库读写操作，事务中所做的操作
         * 其他事务都可以看到
         */
        RW_TRANSACTION
        
    }