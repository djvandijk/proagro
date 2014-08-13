package isf.proagro.reader;

import java.util.EventObject;

/**
 * Created by Scrappy on 13-12-13.
 */
public class UpdateLibraryEvent extends EventObject {

    public enum UpdateType
    {
        START,
        GENERIC,
        MINOR,
        DOWNLOAD_PROGRESS,
        BOOKLET_INFO,
        COVER,
        PDF,
        FINISHED,
        ERROR
    }

    public UpdateType Type;

    public String Message;

    public UpdateLibraryEvent(Object source, UpdateType updateType)
    {
        this(source, updateType, "");
    }

    public UpdateLibraryEvent(Object source, UpdateType updateType, String message)
    {
        super(source);
        Type = updateType;
        Message = message;
    }
}
