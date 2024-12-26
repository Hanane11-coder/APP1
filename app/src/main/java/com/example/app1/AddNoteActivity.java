package com.example.app1;

import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {
    private EditText titleEditText, contentEditText;
    private ImageView noteImageView;
    private Button saveButton, addImageButton;
    private NoteDBHelper dbHelper;
    private byte[] imageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        dbHelper = new NoteDBHelper(this);
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        noteImageView = findViewById(R.id.noteImageView);
        saveButton = findViewById(R.id.saveButton);
        addImageButton = findViewById(R.id.addImageButton);

        addImageButton.setOnClickListener(v -> openImagePicker());

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();
            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Le titre et le contenu ne peuvent pas être vides", Toast.LENGTH_SHORT).show();
            } else {
                if (imageByteArray == null) {
                    imageByteArray = new byte[0]; // Pas d'image ajoutée
                }
                dbHelper.addNote(title, content, imageByteArray);
                Toast.makeText(this, "Note ajoutée", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageByteArray = imageToByteArray(bitmap);
                noteImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] imageToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
