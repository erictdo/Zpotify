//package main.java.dfs;

import java.util.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;

/* JSON Format

{"file":
  [
     {"name":"MyFile",
      "size":128000000,
      "pages":
      [
         {
            "guid":11,
            "size":64000000
         },
         {
            "guid":13,
            "size":64000000
         }
      ]
      }
   ]
} 
*/

public class DFS {

    Date date;

    /**
     * After page has been mapped, remove it from the file's pages in metadata
     *
     * @param file - Name of the file being edited
     * @throws Exception
     */
    public void onPageComplete(String file) throws Exception {
        FilesJson metadata = this.readMetaData();
        for (int i = metadata.file.size() - 1; i >= 0; i--) {
            if (metadata.file.get(i).getName().equals(file)) {
                metadata.file.get(i).decrementRef();
                break;
            }
        }
        writeMetaData(metadata);
    }

    public class PagesJson {

        String lowerBoundInterval;
        private Long guid;
        private Long size;
        private String name;
        private Long creationTS;
        private Long readTS;
        private Long writeTS;
        private int referenceCount;

        public PagesJson() {
            size = 0L;
            guid = 0L;
            creationTS = date.getTime();
            readTS = date.getTime();
            writeTS = date.getTime();
            referenceCount = 0;
        }

        public PagesJson(Long guid, Long size) {
            this.size = size;
            this.guid = guid;

            creationTS = date.getTime();
            readTS = date.getTime();
            writeTS = date.getTime();
            referenceCount = 0;
        }

        // getters
        public Long getGUID() {
            return guid;
        }

        public String getName() {
            return name;
        }

        public Long getSize() {
            return size;
        }

        // setters
        public void setGUID(Long guid) {
            this.guid = guid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSize(Long size) {
            this.size = size;
        }
    };

    public class FileJson {
        private String name;
        private Long size;
        private Long creationTS;
        private Long readTS;
        private Long writeTS;
        private int referenceCount;
        private int numberOfPages;
        private int maxPageSize;
        ArrayList<PagesJson> pages;

        public FileJson() {

        }

        public FileJson(String name, Long size) {
            this.name = name;
            this.size = size;

            this.numberOfPages = 0;
            this.referenceCount = 0;
            pages = new ArrayList<PagesJson>();

            // Set timestamps
            this.creationTS = date.getTime();
            this.readTS = date.getTime();
            this.writeTS = date.getTime();
        }

        /**
         * Decrement object's reference count when object is no longer in use
         */
        public void decrementRef() {
            this.referenceCount--;
        }

        public void decrementRef(int index) {
            pages.get(index).referenceCount--;
        }

        public void incrementRef() {
            this.referenceCount++;
        }

        public void incrementRef(int index) {
            this.referenceCount++;
        }

        public void addPage(PagesJson page, Long addSize) {
            pages.add(page);
            numberOfPages++;
            size += addSize;
        }

        // getters
        public String getName() {
            return name;
        }

        public Long getSize() {
            return size;
        }

        public Long getWriteTS() {
            return writeTS;
        }

        public int getReferenceCount() {
            return referenceCount;
        }

        public int getNumberOfPages() {
            return numberOfPages;
        }

        public int getMaxPageSize() {
            return maxPageSize;
        }

        // setters
        public void setName(String name) {
            this.name = name;
        }

        public void setSize(Long size) {
            this.size = size;
        }

        public void setWriteTS(Long writeTS) {
            this.writeTS = writeTS;
        }

        public void setReferenceCount(int refCount) {
            referenceCount = refCount;
        }

        public void setNumberOfPages(int numberOfPages) {
            this.numberOfPages = numberOfPages;
        }

        public void setMaxPageSize(int maxPageSize) {
            this.maxPageSize = maxPageSize;
        }
    };

    public class FilesJson {
        private String name;
        List<FileJson> file;

        public FilesJson() {
            file = new ArrayList<FileJson>();
        }

        // getters
        public String getName() {
            return name;
        }

        public List<FileJson> getFiles() {
            return file;
        }

        // setters
        public void setName(String name) {
            this.name = name;
        }

        public void setFiles(List<FileJson> file) {
            this.file = file;
        }
    };

    int port;
    Chord chord;

    private long md5(String objectName) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1, m.digest());
            return Math.abs(bigInt.longValue());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }
        return 0;
    }

    public DFS(int port) throws Exception {

        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid + "/repository"));
        Files.createDirectories(Paths.get(guid + "/tmp"));
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                chord.leave();
            }
        });

    }

    /**
     * Join the chord
     *
     */
    public void join(String Ip, int port) throws Exception {
        chord.joinRing(Ip, port);
        chord.print();
    }

    /**
     * leave the chord
     *
     */
    public void leave() throws Exception {
        chord.leave();
    }

    /**
     * print the status of the peer in the chord
     *
     */
    public void print() throws Exception {
        chord.print();
    }

    /**
     * readMetaData read the metadata from the chord
     *
     */
    public FilesJson readMetaData() throws Exception {
        FilesJson filesJson = null;
        try {
            Gson gson = new Gson();
            long guid = md5("Metadata");

            System.out.println("GUID " + guid);
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            RemoteInputFileStream metadataraw = peer.get(guid);
            metadataraw.connect();
            Scanner scan = new Scanner(metadataraw);
            scan.useDelimiter("\\A");
            String strMetaData = scan.next();
            System.out.println(strMetaData);
            filesJson = gson.fromJson(strMetaData, FilesJson.class);
        } catch (Exception ex) {
            filesJson = new FilesJson();
        }
        return filesJson;
    }

    /**
     * writeMetaData write the metadata back to the chord
     *
     */
    public void writeMetaData(FilesJson filesJson) throws Exception {
        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);

        Gson gson = new Gson();
        peer.put(guid, gson.toJson(filesJson));
    }

    /**
     * Change Name
     *
     */
    public void move(String oldName, String newName) throws Exception {
        // TODO: Change the name in Metadata
        // Write Metadata
        FilesJson metadata = readMetaData();

        for (FileJson file : metadata.file) {
            if (file.getName().equals(oldName)) {
                file.incrementRef(); // Increment referenceCount
                writeMetaData(metadata); // Update metadata with new reference count
                file.setName(newName); // Change old file name to newName
                file.setWriteTS(date.getTime()); // Update write timestamp
                file.decrementRef(); // Decrement referenceCount
                writeMetaData(metadata); // Update metadata
                break;
            }
        }
    }

    /**
     * List the files in the system
     *
     */
    public String lists() throws Exception {
        FilesJson fileJson = readMetaData();
        String listOfFiles = "";

        for (FileJson f : fileJson.file) {
            listOfFiles += f.getName() + "\n";
        }

        return listOfFiles;
    }

    /**
     * create an empty file
     *
     * @param fileName Name of the file
     */
    public void create(String fileName) throws Exception {
        // Write Metadata

        FileJson newFile = new FileJson(fileName, 0L);

        FilesJson metaData = readMetaData();

        metaData.file.add(newFile);

        writeMetaData(metaData);
    }

    /**
     * delete file
     *
     * @param fileName Name of the file
     */
    public void delete(String fileName) throws Exception {
        FilesJson metadata = readMetaData();

        boolean found = false;

        for (FileJson file : metadata.file) {
            if (file.getName().equals(fileName)) {
                for (PagesJson page : file.pages) {
                    ChordMessageInterface peer = chord.locateSuccessor(page.getGUID()); // Locate node where page is
                                                                                        // held
                    peer.delete(page.getGUID()); // Delete page
                }

                // Remove file from metadata
                metadata.file.remove(file);
                found = true;
            }
        }

    }

    /**
     * Read block pageNumber of fileName
     *
     * @param fileName   Name of the file
     * @param pageNumber number of block.
     */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file
        boolean find = false;
        FileJson myFile = null;
        int fileIndex = 0;
        for (FileJson file : metadata.file) {
            if (file.getName().equals(fileName)) {
                myFile = file;
                if (myFile.getNumberOfPages() == 0 || pageNumber >= myFile.getNumberOfPages()) {
                    return null;
                }

                file.incrementRef(pageNumber); // Increment file and page refCount
                file.readTS = new Date().getTime(); // Update file read timestamp
                file.pages.get(pageNumber).readTS = new Date().getTime(); // Update page read timestamp
                writeMetaData(metadata); // Update metadata with refCount and timestamps
                find = true;
                break;
            }
        }

        // If file was found return page, else return null
        if (find) {
            PagesJson myPage = myFile.pages.get(pageNumber);
            metadata.file.get(fileIndex).decrementRef(pageNumber); // Decrement refCount
            writeMetaData(metadata); // Update metadata for read and refCount
            ChordMessageInterface peer = chord.locateSuccessor(myPage.getGUID());
            return peer.get(myPage.guid);
        } else
            return null;
    }

    /**
     * Add a page to the file
     *
     * @param fileName Name of the file
     * @param data     RemoteInputStream.
     */
    public void append(String fileName, RemoteInputFileStream data) throws Exception {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file
        boolean find = false;
        int newPageIndex = 0;
        int fileIndex = 0;
        Long pageGUID = Long.valueOf(0);
        for (int i = 0; i < metadata.file.size(); i++) {
            if (metadata.file.get(i).getName().equals(fileName)) {
                fileIndex = i;
                metadata.file.get(i).incrementRef(); // Increment file refCount
                writeMetaData(metadata); // Write updated metadata
                newPageIndex = metadata.file.get(i).pages.size();
                pageGUID = md5(fileName + newPageIndex);
                metadata.file.get(i).addPage(new PagesJson(pageGUID, (long) data.total), (long) data.total); // Add new
                                                                                                             // page
                                                                                                             // entry to
                                                                                                             // file and
                                                                                                             // update
                                                                                                             // filesize
                metadata.file.get(i).writeTS = date.getTime(); // Update file write timestamp
                find = true;
                break;
            }
        }

        // If file was found append data and add to chord, else return
        if (find) {
            // Find closest successor node and place data
            metadata.file.get(fileIndex).decrementRef(); // Decrement refcount
            ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
            writeMetaData(metadata); // Update metadata for write and refCount
            peer.put(pageGUID, data);

            System.out.println("Append Complete");
        } else
            return;
    }
}
