package com.alexandr7035.txnotes.utils;

import com.alexandr7035.txnotes.db.NoteEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotesSorter {

    public static void sortByModificationDate(List<NoteEntity> notesList) {
        Collections.sort(notesList, new Comparator<NoteEntity>() {
            @Override
            public int compare(NoteEntity lhs, NoteEntity rhs) {
                if (lhs.getNoteModificationDate() > rhs.getNoteModificationDate()) {
                    return 1;
                }
                else if (lhs.getNoteModificationDate() < rhs.getNoteModificationDate()){
                    return -1;
                }

                else {
                    return 0;
                }
            }
        });
    }

    public static void sortByModificationDateDesc(List<NoteEntity> notesList) {
        Collections.sort(notesList, new Comparator<NoteEntity>() {
            @Override
            public int compare(NoteEntity lhs, NoteEntity rhs) {
                if (lhs.getNoteModificationDate() > rhs.getNoteModificationDate()) {
                    return -1;
                }
                else if (lhs.getNoteModificationDate() < rhs.getNoteModificationDate()){
                    return 1;
                }

                else {
                    return 0;
                }
            }
        });
    }

    public static void sortByText(List<NoteEntity> notesList) {
        Collections.sort(notesList, new Comparator<NoteEntity>() {
            @Override
            public int compare(NoteEntity lhs, NoteEntity rhs) {
                // FIXME use titles
                return lhs.getNoteText().compareTo(rhs.getNoteText());
            }
        });
    }

}
