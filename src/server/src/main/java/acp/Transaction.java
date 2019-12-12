package main.java.acp;

import java.sql.Timestamp;

public class Transaction {
    public enum Operation {WRITE, DELETE, READ};
    //public enum Vote {YES, NO};
    Long transactionID;
    //Vote vote;
    Operation operation;
    String fileName;
    int pageIndex;
    Long timestamp;

    public Transaction(Long transactionID, String fileName, int pageIndex, Operation o, Long timestamp)
    {
       this.transactionID = transactionID;
       this.fileName = fileName;
       this.pageIndex = pageIndex;
       operation = o;
       this.timestamp = timestamp;
    }

    public Long getTransactionID()
    {
        return transactionID;
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getPageIndex()
    {
        return pageIndex;
    }

//    public Vote getVote()
//    {
//        return vote;
//    }

    public Long getTimestamp() { return timestamp;
    }
    public Operation getOperation()
    {
        return operation;
    }
}
