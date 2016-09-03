package isf.proagro.android.rest;


import isf.proagro.android.model.Booklet;
import isf.proagro.android.model.Library;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by eddyhugues on 15-05-17.
 */
public interface RestAPI {

    @GET("/library-new/{lang}/library.xml")
    Observable<Library> getLibrary(@Path("lang") String lang);

    @GET("/library-new/{lang}/{path}")
    Observable<Booklet> getBooklet(@Path("lang") String lang, @Path("path") String path);

    @GET("/library-new/{lang}/{path}")
    Observable<Response> getBookletPDF(@Path("lang") String lang, @Path("path") String path);

}
