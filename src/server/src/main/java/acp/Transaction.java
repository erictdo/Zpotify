package main.java.acp;

public class Transaction {
    public enum Operation {WRITE, DELETE, READ};
    //public enum Vote {YES, NO};
    Long transactionID;
    //Vote vote;
    Operation operation;
    String fileName;
    int pageIndex;

    public Transaction(Long transactionID, String fileName, int pageIndex, Operation o)
    {
       this.transactionID = transactionID;
       this.fileName = fileName;
       this.pageIndex = pageIndex;
       operation = o;
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

    public Operation getOperation()
    {
        return operation;
    }
}
