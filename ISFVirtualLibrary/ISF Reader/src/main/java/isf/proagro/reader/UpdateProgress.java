package isf.proagro.reader;

/**
 * Created by Scrappy on 17-12-13.
 */
public class UpdateProgress {
    public UpdateLibraryEvent.UpdateType Type;

    public String Message;

    public UpdateProgress(UpdateLibraryEvent.UpdateType type)
    {
        this(type, "");
    }

    public UpdateProgress(UpdateLibraryEvent.UpdateType type, String message)
    {
        Type = type;
        Message = message;
    }
}
