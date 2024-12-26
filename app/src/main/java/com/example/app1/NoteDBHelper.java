package com.example.app1;
public class NoteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_ARCHIVED = "archived";
    public static final String COLUMN_IMAGE = "image"; // Colonne pour l'image
    public static final String COLUMN_DELETED = "deleted"; // Colonne pour l'état de suppression

    public NoteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_ARCHIVED + " INTEGER DEFAULT 0,"
                + COLUMN_IMAGE + " BLOB,"
                + COLUMN_DELETED + " INTEGER DEFAULT 0" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    // Ajouter une note
    public void addNote(String title, String content, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_ARCHIVED, 0);  // Par défaut, la note n'est pas archivée
        values.put(COLUMN_DELETED, 0);   // Par défaut, la note n'est pas supprimée

        db.insert(TABLE_NOTES, null, values);
    }

    // Modifier une note
    public void updateNote(int id, String title, String content, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_IMAGE, image);

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Supprimer une note (en la marquant comme supprimée)
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED, 1); // Marquer la note comme supprimée

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Archiver une note
    public void archiveNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ARCHIVED, 1); // Marquer la note comme archivée

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Récupérer toutes les notes (ne pas afficher celles supprimées)
    public List<Note> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_DELETED + " = 0", null);
        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            boolean archived = cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVED)) == 1;
            boolean deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED)) == 1;
            byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));

            notes.add(new Note(id, title, content, archived, deleted, image));
        }
        cursor.close();
        return notes;
    }

    // Récupérer les notes archivées
    public List<Note> getArchivedNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_ARCHIVED + " = 1 AND " + COLUMN_DELETED + " = 0", null);
        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            boolean archived = cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVED)) == 1;
            boolean deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED)) == 1;
            byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));

            notes.add(new Note(id, title, content, archived, deleted, image));
        }
        cursor.close();
        return notes;
    }

    // Récupérer les notes supprimées (corbeille)
    public List<Note> getDeletedNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES + " WHERE " + COLUMN_DELETED + " = 1", null);
        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            boolean archived = cursor.getInt(cursor.getColumnIndex(COLUMN_ARCHIVED)) == 1;
            boolean deleted = cursor.getInt(cursor.getColumnIndex(COLUMN_DELETED)) == 1;
            byte[] image = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));

            notes.add(new Note(id, title, content, archived, deleted, image));
        }
        cursor.close();
        return notes;
    }

    // Restaurer une note de la corbeille
    public void restoreNoteFromTrash(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DELETED, 0); // Restaurer la note (marquer comme non supprimée)

        db.update(TABLE_NOTES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Supprimer définitivement une note
    public void permanentDeleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
