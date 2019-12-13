package main.java.dfs;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.*;
import java.math.BigInteger;
import java.security.*;
import com.google.gson.Gson;
import main.java.acp.Transaction;


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


public class DFS
{

    Date date = new Date();

    // Init index
    char[] index = new char[38];

    public void initIndex() {
        for (int i = 65; i <= 90; i++)
            index[i - 65] = (char) i;
        for (int i = 48; i <= 57; i++)
            index[i - 22] = (char) i;
        index[36] = '-';
        index[37] = '+';
    }

    public char[] getIndex() throws Exception {
        return index;
    }

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

    public class PagesJson
    {

        String lowerBoundInterval;
        private Long    guid;
        private Long    size;
        private String  name;
        private Long    creationTS;
        private Long    readTS;
        private Long    writeTS;
        private int     referenceCount;

        public PagesJson()
        {
            size = 0L;
            guid = 0L;
            creationTS = date.getTime();
            readTS = date.getTime();
            writeTS = date.getTime();
            referenceCount = 0;
        }

        public PagesJson(Long guid, Long size)
        {
            this.size = size;
            this.guid = guid;

            creationTS = date.getTime();
            readTS = date.getTime();
            writeTS = date.getTime();
            referenceCount = 0;
        }

        public PagesJson(Long guid, Long size, String interval) {
            this(guid, size);
            this.lowerBoundInterval = interval;
        }

        // getters
        public Long getGUID() { return guid; }
        public String getName() { return name; }
        public Long getSize() { return size; }
        // setters
        public void setGUID(Long guid) { this.guid = guid; }
        public void setName(String name) { this.name = name; }
        public void setSize(Long size) { this.size = size; }
    };

    public class FileJson
    {
        private String  name;
        private Long    size;
        private Long    creationTS;
        private Long    readTS;
        private Long    writeTS;
        private int     referenceCount;
        private int     numberOfPages;
        private int     maxPageSize;
        ArrayList<PagesJson> pages;

        public FileJson()
        {

        }

        public FileJson(String name, Long size)
        {
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
        public void decrementRef() { this.referenceCount--; }
        public void decrementRef(int index) { this.referenceCount++;
            pages.get(index).referenceCount++; }
        public void incrementRef() { this.referenceCount++; }
        public void incrementRef(int index) { this.referenceCount--;
            pages.get(index).referenceCount--; }

        public void addPage(PagesJson page, Long addSize)
        {
            pages.add(page);
            numberOfPages++;
            size += addSize;
        }
        // getters
        public String getName() { return name; }
        public Long getSize() { return size; }
        public Long getWriteTS() { return writeTS; }
        public int getReferenceCount() { return referenceCount; }
        public int getNumberOfPages() { return numberOfPages; }
        public int getMaxPageSize() { return maxPageSize; }
        public ArrayList<PagesJson> getPages() { return pages; }
        // setters
        public void setName(String name) { this.name = name; }
        public void setSize(Long size) { this.size = size; }
        public void setWriteTS(Long writeTS) { this.writeTS = writeTS; }
        public void setReferenceCount(int refCount) { referenceCount = refCount; }
        public void setNumberOfPages(int numberOfPages) { this.numberOfPages = numberOfPages; }
        public void setMaxPageSize(int maxPageSize) { this.maxPageSize = maxPageSize; }
    };

    public class FilesJson
    {
        private String  name;
        List<FileJson>  file;

        public FilesJson()
        {
            file = new ArrayList<FileJson>();
        }

        // getters
        public String getName() { return name; }
        public List<FileJson> getFiles() { return file; }
        // setters
        public void setName(String name) { this.name = name; }
        public void setFiles(List<FileJson> file) { this.file = file; }

        public void addFile(FileJson f) {
            this.file.add(f);
        }
    };


    int port;
    Chord  chord;

    public int getPort()
    {
        return port;
    }

    public Chord getChord()
    {
        return chord;
    }


    private long md5(String objectName)
    {
        try
        {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(objectName.getBytes());
            BigInteger bigInt = new BigInteger(1,m.digest());
            return Math.abs(bigInt.longValue());
        }
        catch(NoSuchAlgorithmException e)
        {
                e.printStackTrace();

        }
        return 0;
    }



    public DFS(int port) throws Exception
    {


        this.port = port;
        long guid = md5("" + port);
        chord = new Chord(port, guid);
        Files.createDirectories(Paths.get(guid+"/repository"));
        Files.createDirectories(Paths.get(guid+"/tmp"));
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
    public void join(String Ip, int port) throws Exception
    {
        chord.joinRing(Ip, port);
        chord.print();
    }


   /**
 * leave the chord
  *
 */
    public void leave() throws Exception
    {
       chord.leave();
    }

   /**
 * print the status of the peer in the chord
  *
 */
    public void print() throws Exception
    {
        chord.print();
    }

/**
 * readMetaData read the metadata from the chord
  *
 */
    public FilesJson readMetaData() throws Exception
    {
        FilesJson filesJson = null;
        long guid = md5("Metadata");

        try {
            Gson gson = new Gson();

            System.out.println("GUID " + guid);
            ChordMessageInterface peer = chord.locateSuccessor(guid);
            RemoteInputFileStream metadataraw = peer.get(guid);
            metadataraw.connect();
            Scanner scan = new Scanner(metadataraw);
            scan.useDelimiter("\\A");
            String strMetaData = scan.next();
            System.out.println(strMetaData);
            filesJson= gson.fromJson(strMetaData, FilesJson.class);
        } catch (Exception ex)
        {
            File metadata = new File(this.chord.getPrefix() + guid);       // Create file object with filepath
            metadata.createNewFile();                                         // Create the physical file

            // Create initial data for metadata
            filesJson = new FilesJson();
            filesJson.addFile(new FileJson("Metadata", Long.valueOf(0)));   // Add metadata entry

            // Write data to metadata file
            writeMetaData(filesJson);
        }
        return filesJson;
    }

/**
 * writeMetaData write the metadata back to the chord
  *
 */
    public void writeMetaData(FilesJson filesJson) throws Exception
    {
        long guid = md5("Metadata");
        ChordMessageInterface peer = chord.locateSuccessor(guid);

        Gson gson = new Gson();
        peer.put(guid, gson.toJson(filesJson));
    }

    /**
     * Change Name
     *
     */
    public void move(String oldName, String newName) throws Exception
    {
        // TODO:  Change the name in Metadata
        // Write Metadata
        FilesJson metadata = readMetaData();

        for (int i = 0; i < metadata.file.size(); i++) {
            if (metadata.file.get(i).getName().equals(oldName)) {
                System.out.println("Move file found!\n");
                metadata.file.get(i).incrementRef();                    // Increment referenceCount
                writeMetaData(metadata);                                // Update metadata with new reference count
                metadata.file.get(i).name = newName;                    // Change old file name to newName
                metadata.file.get(i).writeTS = date.getTime();          // Update write timestamp
                metadata.file.get(i).decrementRef();                    // Decrement referenceCount
                writeMetaData(metadata);                                // Update metadata
                break;
            }
        }
    }


    /**
     * List the files in the system
     *
     */
    public String lists() throws Exception
    {
        StringBuilder listOfFiles = new StringBuilder("\nFiles:\n");                        // Initialize string to hold file names

        List<FileJson> myFiles = readMetaData().file;               // Get our list of files
        for (int i = 0; i < myFiles.size(); i++) {
            listOfFiles.append(myFiles.get(i).name + "\n");              // Append each file name
        }
        listOfFiles.append("\n");
        return listOfFiles.toString();
    }

    /**
     * create an empty file
     *
     * @param fileName Name of the file
     */
    public void create(String fileName) throws Exception
    {
        // Write Metadata

        FileJson newFile = new FileJson(fileName,0L);

        FilesJson metaData = readMetaData();

        metaData.file.add(newFile);

        writeMetaData(metaData);
    }

    /**
     * delete file
     *
     * @param fileName Name of the file
     */
    public void delete(String fileName) throws Exception
    {
        FilesJson metadata = readMetaData();

        boolean found = false;

        for (int i = 0; i < metadata.file.size(); i++) {
            if (metadata.file.get(i).getName().equals(fileName)) {

                // Delete physical pages for file from chord
                for (int j = 0; j < metadata.file.get(i).pages.size(); j++) {
                    ChordMessageInterface peer = chord.locateSuccessor(metadata.file.get(i).pages.get(j).getGUID());        // Locate node where page is held
                    peer.delete(metadata.file.get(i).pages.get(j).getGUID());                       // Delete page
                }

                // Remove file from metadata
                metadata.file.remove(i);
                found = true;
            }
        }

        // Write Metadata if file was found, else return
        if (found) {
            System.out.println("File:\t" + fileName + " deleted!\n");
            writeMetaData(metadata);
        } else return;

    }

    /**
     * Read block pageNumber of fileName
     *
     * @param fileName Name of the file
     * @param pageNumber number of block.
     */
    public RemoteInputFileStream read(String fileName, int pageNumber) throws Exception
    {
        // Read Metadata
        FilesJson metadata = readMetaData();

        // Find file
        boolean find = false;
        FileJson myFile = null;
        int fileIndex = 0;
        for (int i = 0; i < metadata.file.size(); i++) {
            if (metadata.file.get(i).getName().equals(fileName)) {
                fileIndex = i;
                myFile = metadata.file.get(i);
                if (myFile.numberOfPages == 0 || pageNumber >= myFile.numberOfPages) {
                    return null;
                }

                metadata.file.get(i).incrementRef(pageNumber);                              // Increment file and page refCount
                metadata.file.get(i).readTS = new Date().getTime();                              // Update file read timestamp
                metadata.file.get(i).pages.get(pageNumber).readTS = new Date().getTime();         // Update page read timestamp
                writeMetaData(metadata);                                                    // Update metadata with refCount and timestamps
                find = true;
                break;
            }
        }

        // If file was found return page, else return null
        if (find) {
            PagesJson myPage = myFile.pages.get(pageNumber);
            metadata.file.get(fileIndex).decrementRef(pageNumber);                          // Decrement refCount
            writeMetaData(metadata);                                                        // Update metadata for read and refCount
            ChordMessageInterface peer = chord.locateSuccessor(myPage.getGUID());
            return peer.get(myPage.guid);
        } else return null;
    }

    /**
     * Add a page to the file
     *
     * @param fileName Name of the file
     * @param data RemoteInputStream.
     */
    public void append(String fileName, RemoteInputFileStream data) throws Exception
    {
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
                metadata.file.get(i).incrementRef();                                            // Increment file refCount
                writeMetaData(metadata);                                                        // Write updated metadata
                newPageIndex = metadata.file.get(i).pages.size();
                pageGUID = md5(fileName + newPageIndex);
                metadata.file.get(i).addPage(new PagesJson(pageGUID, (long) data.total), (long) data.total);     // Add new page entry to file and update filesize
                metadata.file.get(i).writeTS = date.getTime();              // Update file write timestamp
                find = true;
                break;
            }
        }

        // If file was found append data and add to chord, else return
        if (find) {
            //Find closest successor node and place data
            metadata.file.get(fileIndex).decrementRef();                                    // Decrement refcount
            ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
            writeMetaData(metadata);                                                        // Update metadata for write and refCount
            peer.put(pageGUID, data);

            System.out.println("Append Complete");
        } else return;
    }
    /**
     * Appends a string marked by single quotes (') as a page to the target file
     *
     * @param filename - The file to add a page to.
     * @param text     - The text to append.
     * @throws Exception
     */

    public void append(String filename, String text) throws Exception {
        FilesJson metadata = readMetaData();

        FileJson target = find(metadata.getFiles(), filename);
        if (target != null) {
            long timestamp = new Date().getTime();
            long pageGUID = md5(filename + timestamp);
            long text_len = (long) text.getBytes("UTF-8").length;

            PagesJson page = new PagesJson(pageGUID, text_len);

            target.writeTS = timestamp;                 // Update write timestamp
            target.addPage(page, text_len);     // Add a new page to the file
            target.incrementRef();

            ChordMessageInterface peer = chord.locateSuccessor(pageGUID);
            peer.put(pageGUID, text);                   // Store the file on the Chord

            target.decrementRef();
            writeMetaData(metadata);
        } else {
            System.out.println("File '" + filename + "' not found.");
        }
    }

    public String search(long pageGuid, String song) throws Exception{
        String result = chord.search(pageGuid, song);
        System.out.println(result);
        return result;
    }

    /**
     * Finds the specified file in the Metadata.
     */
    private FileJson find(List<FileJson> files, String filename) {
        for (var item : files) {
            if (item.name.equals(filename)) {
                return item;
            }
        }
        return null;
    }

    public void replicateFile(String fileName, String text) throws Exception {
        int numOfReplicas = 3;
        for (int i = 0 ; i < numOfReplicas ; i++) {
            append(fileName, text);
        }
    }

    public void push(String fileName, int pageNumber) throws Exception {

        // Read Metadata
        FilesJson metadata = readMetaData();
        FileJson foundFile = null;
        PagesJson foundPage;
        // Find file
        boolean find = false;
        int newPageIndex = 0;
        int fileIndex = 0;
        Long pageGUID = Long.valueOf(0);
        for (int i = 0; i < metadata.file.size(); i++) {
            if (metadata.file.get(i).getName().equals(fileName)) {
                fileIndex = i;
                metadata.file.get(i).incrementRef();                                            // Increment file refCount
                writeMetaData(metadata);                                                        // Write updated metadata
                foundFile = metadata.file.get(i);
                find = true;
                break;
            }
        }

        foundPage = foundFile.pages.get(pageNumber);

        try {
//            long timestamp = new Date().getTime();

            Transaction trans = new Transaction(foundPage.getGUID(), fileName, Transaction.Operation.WRITE, foundPage.writeTS);

            if (chord.canCommit(trans))  {
                Long newWriteTS = new Date().getTime();
                trans.setTimestamp(newWriteTS);
                chord.doCommit(trans);
                foundPage.writeTS = newWriteTS;
                foundFile.writeTS = newWriteTS;
                foundFile.pages.set(pageNumber, foundPage);
                metadata.file.set(fileIndex, foundFile);
            }
            writeMetaData(metadata);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public File pull(String fileName, int pageNumber) {

        RemoteInputFileStream rifs = null;

        try {
            long timestamp = new Date().getTime();

            Transaction trans = new Transaction(chord.guid, fileName, Transaction.Operation.READ, timestamp);

            if (chord.canCommit(trans))  {
                rifs = read(fileName, pageNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return null;
    }
}
