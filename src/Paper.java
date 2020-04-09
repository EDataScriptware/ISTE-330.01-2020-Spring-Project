import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Row Abstraction Data Access Object for a Paper
 *
 * @author Liam Bewley
 * @version 2020.04.09
 */

public class Paper {

    //attributes
    private int paperID;
    private String title;
    private String paperAbstract;
    private String track;
    private String status;
    private String type;
    private int submitterID;
    private String fileID;
    private String tentativeStatus;
    private ArrayList<String> authors; //change to ArrayList<User> once User created?
    private ArrayList<String> subjects;

    //constructors
    public Paper() {

    }

    public Paper(int paperID) {
        this.paperID  = paperID;
    }

    //getters and setters


    public int getPaperID() {
        return paperID;
    }

    public void setPaperID(int paperID) {
        this.paperID = paperID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return paperAbstract;
    }

    public void setAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSubmitterID() {
        return submitterID;
    }

    public void setSubmitterID(int submitterID) {
        this.submitterID = submitterID;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getTentativeStatus() {
        return tentativeStatus;
    }

    public void setTentativeStatus(String tentativeStatus) {
        this.tentativeStatus = tentativeStatus;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }

    private MySQLDatabase connect() throws DLException {
        MySQLDatabase db = new MySQLDatabase();
        db.connect();
        return db;
    }

    /**
     * Update class data to database using paperID
     */
    public void fetch() throws DLException {

        try {
            //connect
            MySQLDatabase db = connect();

            //get data
            ArrayList<String> values = new ArrayList<>();
            ArrayList<String> list = db.getData("SELECT * FROM papers WHERE paperID = " + getPaperID()).get(0);

            int submissionTypeID;

            if (list.size() > 2) {
                //set data to match
                setTitle(list.get(1));
                setAbstract(list.get(2));
                setTrack(list.get(3));
                setStatus(list.get(4));
                submissionTypeID = Integer.parseInt(list.get(5));
                setSubmitterID(Integer.parseInt(list.get(6)));
                setFileID(list.get(7));
                setTentativeStatus(list.get(8));

            } else {
                throw new DLException("No data returned");
            }

            //get submission type
            setType(db.getData("SELECT typeName FROM _Types where typeId = " + submissionTypeID).get(0).get(0));

            //get authors
            setAuthors(db.getData("SELECT userId FROM PaperAuthors WHERE paperId = " + getPaperID()).get(0));

            //get subjects
            setSubjects(db.getData("SELECT subjectId FROM PaperSubjects WHERE paperId = " + getPaperID()).get(0));

            db.close();
        } catch (Exception e) {
            throw new DLException(e, "Requested operation failed");
        }
    }

    /**
     * Update database entry for this ID
     *
     * @return Number of records affected
     */
    public int put() throws DLException {

        //connect
        MySQLDatabase db = connect();

        //list values for papers
        ArrayList<String> values = new ArrayList<>();
        values.add(getTitle());
        values.add(getAbstract());
        values.add(getTrack());
        values.add(getStatus());
        values.add(db.getData("SELECT typeId FROM _Types WHERE typeName = " + getType()).get(0).get(0));
        values.add("" + getSubmitterID());
        values.add("" + getFileID());
        values.add(getTentativeStatus());
        values.add("" + getPaperID());

        //put data for papers
        int r = db.setData("UPDATE paper SET title = ?, abstract = ?, track = ?, status = ?,"
            + "submissionType = ?, submitterId = ?, fileId = ?, tentativeStatus = ?  WHERE paperID = ?", values);

        //update paperAuthors


        //update paperSubjects


        db.close();
        return r;

    }

    /**
     * Insert this equipment object into DB
     *
     * @return Number of records affected
     */
    public int post() throws DLException {

        //connect
        MySQLDatabase db = connect();

        //list values
        ArrayList<String> values = new ArrayList<>();
        values.add("" + getPaperID());
        values.add(getTitle());
        values.add(getAbstract());
        values.add(getTrack());
        values.add(getStatus());
        values.add(db.getData("SELECT typeId FROM _Types WHERE typeName = " + getType()).get(0).get(0));
        values.add("" + getSubmitterID());
        values.add("" + getFileID());
        values.add(getTentativeStatus());

        //post data
        int r = db.setData( "INSERT INTO papers VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", values);

        db.close();
        return r;
    }

    /**
     * Delete this equipment object from DB
     *
     * @return Number of records affected
     */
    public int delete() throws DLException {

        //connect
        MySQLDatabase db = connect();

        //list for setData
        ArrayList<String> values = new ArrayList<>();
        values.add("" + getPaperID());

        //delete
        int r = db.setData("DELETE FROM papers WHERE paperId = ?", values);

        db.close();
        return r;

    }

    /**
     * Get all info (except filename) for specified paper
     *
     * @param paperID id for paper to get info on
     * @return List containing all info
     *  private int paperID;
     *     private String title;
     *     private String paperAbstract;
     *     private String track;
     *     private String status;
     *     private String type;
     *     private int submitterID;
     *     private String fileID;
     *     private String tentativeStatus;
     *     private ArrayList<Integer> authors; //change to ArrayList<User> once User created?
     *     private ArrayList<String> subjects;
     */
    public ArrayList<String> getPaper(int paperID) throws DLException {

        //paper by ID
        Paper paper = new Paper(paperID);
        paper.fetch();

        ArrayList<String> paperInfo = new ArrayList<String>();

        //add basic attributes
        paperInfo.add("" + getPaperID());
        paperInfo.add(getTitle());
        paperInfo.add(getAbstract());
        paperInfo.add(getTrack());
        paperInfo.add(getStatus());
        paperInfo.add(getType());
        paperInfo.add("" + getSubmitterID());
        paperInfo.add(getTentativeStatus());

        //add authors
        String authors = "";
        for(String author: getAuthors()) {
            authors += "" + author + ", ";
        }
        paperInfo.add(authors);

        //add subjects
        String subjects = "";
        for(String subject: getSubjects()) {
            subjects += subject + ", ";
        }
        paperInfo.add(subjects);

        return paperInfo;

    }

    /**
     *
     * @param paperID paperId
     * @param submissionTitle Paper Title
     * @param submissionAbstract Paper Abstract
     * @param submissionType Paper Type
     * @param filename Filename of paper
     * @param subjects Paper subjects
     * @param coauthorFirstNames First names of coauthors
     * @param coauthorLastNames Last names of coauthors
     * @return
     * @throws DLException
     */
    public boolean setPaper(int paperID,
                            String submissionTitle,
                            String submissionAbstract,
                            int submissionType,
                            String filename,
                            String[] subjects,
                            String[] coauthorFirstNames,
                            String[] coauthorLastNames) throws DLException {

        boolean successful = false;

            MySQLDatabase db = connect();

            //simple attributes
            setPaperID(paperID);
            setTitle(submissionTitle);
            setAbstract(submissionAbstract);
            setType(db.getData("SELECT typeId FROM _Types WHERE typeName = " + getType()).get(0).get(0));
            setFileID(filename);

            //subjects
            ArrayList<String> subjectsList = new ArrayList<String>();
            for(int i = 0; i < subjects.length; i++) {
                subjectsList.add(subjects[i]);
            }
            setSubjects(subjectsList);

            //authors
            ArrayList<String> authorIDs = new ArrayList<String>();
            for(int i = 0; i < coauthorFirstNames.length; i++) {
                authorIDs.add(db.getData("SELECT userId FROM users WHERE firstName = "
                        + coauthorFirstNames[i] + " AND lastName = " + coauthorLastNames[i]).get(0).get(0));
            }
            setAuthors(authorIDs);

            if(paperID == 0) {
                successful = post() > 0;
            } else {
                successful = put() > 0;
            }

        return successful;

    }

    @Override
    public String toString() {
        return "Paper{" +
                "paperID=" + paperID +
                ", title='" + title + '\'' +
                ", paperAbstract='" + paperAbstract + '\'' +
                ", track='" + track + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", submitterID=" + submitterID +
                ", fileID='" + fileID + '\'' +
                ", tentativeStatus='" + tentativeStatus + '\'' +
                ", authors=" + authors +
                ", subjects=" + subjects +
                '}';
    }
}
