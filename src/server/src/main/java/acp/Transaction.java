package main.java.acp;

import java.sql.Timestamp;

public class Transaction {
    public static Long transactionNumber = 10000L;
    public enum Operation {WRITE, DELETE, READ};
    //public enum Vote {YES, NO};
    Long transactionID;
    Long pageGUID;
    //Vote vote;
    Operation operation;
    String fileName;
    //int pageIndex;
    Long timestamp;

    public Transaction(Long pageGUID, String fileName, Operation o, Long timestamp)
    {
       this.transactionID = transactionNumber++;
       this.pageGUID = pageGUID;
       this.fileName = fileName;
       //this.pageIndex = pageIndex;
       operation = o;
       this.timestamp = timestamp;
    }

    public void setTimestamp(Long newTimestamp) { timestamp = newTimestamp; }

    public Long getTransactionID()
    {
        return transactionID;
    }

    public String getFileName()
    {
        return fileName;
    }

//    public int getPageIndex()
//    {
//        return pageIndex;
//    }

//    public Vote getVote()
//    {
//        return vote;
//    }

    public Long getTimestamp() { return timestamp;
    }
    public Long getPageGUID()
    {
        return pageGUID;
    }
}
