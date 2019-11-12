package main.java.dfs;

import java.util.List;


public interface IDFSInterface {
    void initIndex();

    long md5(String objectName);

    void join(String Ip, int port) throws Exception;

    void leave() throws Exception;

    void print() throws Exception;

    DFS.FilesJson readMetaData() throws Exception;

    void writeMetaData(DFS.FilesJson filesJson) throws Exception;

    void move(String oldName, String newName) throws Exception;

    String lists() throws Exception;

    void create(String fileName) throws Exception;

    void delete(String fileName) throws Exception;

    RemoteInputFileStream read(String fileName, int pageNumber) throws Exception;

    //    /**
    //     * Retrieves a byte array representation of the queried page.
    //     * @param filename Name of the file in the Metadata list.
    //     * @param pageNumber The page to retrieve.
    //     * @param unused_variable Unused.
    //     * @return The page as a byte array.
    //     * @throws Exception
    //     */
    byte[] read(String filename, int pageNumber, int unused_variable) throws Exception;

    //    /**
//     * Retrieves a byte array representation of the queried page.
//     * @param filename Name of the file in the Metadata list.
//     * @param pageNumber The page to retrieve.
//     * @param offset The fragment number.
//     * @param len The max length to be read in the buf
//     * @return The page as a byte array.
//     * @throws Exception
//     */
    byte[] read(String filename, int pageNumber, int offset, int len) throws Exception;

    RemoteInputFileStream head(String fileName) throws Exception;

    RemoteInputFileStream tail(String fileName) throws Exception;

    void append(String fileName, RemoteInputFileStream data) throws Exception;

    void append(String filename, String text) throws Exception;

    void appendEmptyPage(String file, Long page, String lowerBoundInterval) throws Exception;

    DFS.FileJson find(List<DFS.FileJson> collection, String filename);

    void runMapReduce(String fileInput, String fileOutput) throws Exception;

    void onPageComplete(String file) throws Exception;

    void increaseCounter(String file) throws Exception;

    void bulkTree(String file, int size) throws Exception;

    void createFile(String file, int interval, int size) throws Exception;

    public char[] getIndex() throws Exception;

}