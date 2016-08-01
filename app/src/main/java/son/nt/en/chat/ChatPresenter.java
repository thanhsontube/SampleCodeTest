package son.nt.en.chat;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by sonnt on 7/13/16.
 */
public class ChatPresenter implements ChatContract.Presenter
{
    private ChatContract.View mView;
    private FirebaseAuth      mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    public ChatPresenter(@NonNull ChatContract.View view, @NonNull FirebaseAuth mFirebaseAuth,
                    @NonNull DatabaseReference mDatabaseReference)
    {
        this.mView = view;
        this.mFirebaseAuth = mFirebaseAuth;
        this.mDatabaseReference = mDatabaseReference;
    }

    @Override
    public void onStart()
    {

    }

    @Override
    public void sendMessage(String s)
    {
        final FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser == null)
        {
            mView.userDoNotLogin(s);
            return;
        }

        //        FriendlyMessage friendlyMessage = new FriendlyMessage(s, currentUser.getDisplayName(),
        //                        currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "");

        FriendlyMessage friendlyMessage = new FriendlyMessage(currentUser.getUid(), s, currentUser.getDisplayName(),
                        currentUser.getPhotoUrl() != null ? currentUser.getPhotoUrl().toString() : "",
                        System.currentTimeMillis());
        mDatabaseReference.child(ChatFragment.MESSAGES_CHILD).push().setValue(friendlyMessage)
                        .addOnCompleteListener(new OnCompleteListener<Void>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                mView.clearMessage("");
                            }
                        }).addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                            }
                        });

    }
}
