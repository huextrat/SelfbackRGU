package com.traxivity.selfback.traxivity.admin.controller;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.traxivity.selfback.traxivity.admin.model.Message;
import com.traxivity.selfback.traxivity.admin.view.Activities.MessagesManager;

import java.util.ArrayList;

/**
 * Created by Alexandre on 03/05/2017.
 */

public class MessageDAO {

    protected static DatabaseReference mDatabase;
    protected static FirebaseDatabase mFirebaseInstance;
    boolean updated;

    private static MessageDAO INSTANCE = null;

    private MessageDAO() {
        mFirebaseInstance = FirebaseDatabase.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("messages"); //gets the reference of the node 'quotes'
    }

    public final static MessageDAO getInstance(){
        if(INSTANCE == null){
            INSTANCE = new MessageDAO();
        }
        return INSTANCE;
    }

    public static DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public static FirebaseDatabase getmFirebaseInstance() {
        return mFirebaseInstance;
    }

    public boolean addMessage(Message message){
        if(message != null){
            String category = message.getStrBctCategory();

            String messageID =  mDatabase.child(category).child(message.getAchievement().toString()).push().getKey();
            try{
                mDatabase.child(category).child(message.getAchievement().toString()).child(messageID).setValue(message);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
            return true;
        }
        else{
            return false;
        }

    }

    public void getMessages(final MessagesManager.Achievement achievementLevel, final String BCTCategory, final TaskCompletionSource<ArrayList<Message>> messagesGetter){
        final ArrayList<Message> messagesList = new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(BCTCategory)){
                    if(dataSnapshot.child(BCTCategory).hasChild(achievementLevel.toString())){
                        for(DataSnapshot D : dataSnapshot.child(BCTCategory).child(achievementLevel.toString()).getChildren()){
                            messagesList.add(D.getValue(Message.class));
                        }
                    }
                }
                messagesGetter.setResult(messagesList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateMessage(final Message messageToEdit, final Message messageEdited){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(messageToEdit.getStrBctCategory())){
                    if(dataSnapshot.child(messageToEdit.getStrBctCategory()).hasChild(messageToEdit.getAchievement().toString())){
                        for(DataSnapshot D : dataSnapshot.child(messageToEdit.getStrBctCategory()).child(messageToEdit.getAchievement().toString()).getChildren()){
                            Message messageToCheck = D.getValue(Message.class);
                            if(messageToCheck.getContent().equals(messageToEdit.getContent())){
                                if(messageToCheck.getQuote() == null && messageToEdit.getQuote() == null || (messageToCheck.getQuote() != null && messageToCheck.getQuote().getAuthor().equals(messageToEdit.getQuote().getAuthor()) &&  messageToCheck.getQuote().getContent().equals(messageToEdit.getQuote().getContent()))){
                                    mDatabase.child(messageToEdit.getStrBctCategory()).child(messageToEdit.getAchievement().toString()).child(D.getKey()).removeValue();
                                    addMessage(messageEdited);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteMessage(final Message message){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(message.getStrBctCategory())){
                    if(dataSnapshot.child(message.getStrBctCategory()).hasChild(message.getAchievement().toString())){
                        for(DataSnapshot D : dataSnapshot.child(message.getStrBctCategory()).child(message.getAchievement().toString()).getChildren()){
                            Message messageToCheck = D.getValue(Message.class);
                            if(messageToCheck.getContent().equals(message.getContent())){
                                if(messageToCheck.getQuote() == null && message.getQuote() == null || (messageToCheck.getQuote() != null && messageToCheck.getQuote().getAuthor().equals(message.getQuote().getAuthor()) &&  messageToCheck.getQuote().getContent().equals(message.getQuote().getContent()))){
                                    mDatabase.child(message.getStrBctCategory()).child(message.getAchievement().toString()).child(D.getKey()).removeValue();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
