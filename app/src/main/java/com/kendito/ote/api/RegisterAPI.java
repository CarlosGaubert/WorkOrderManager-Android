package com.kendito.ote.api;


import com.kendito.ote.model.Area;
import com.kendito.ote.model.Cliente;
import com.kendito.ote.model.ClienteG;
import com.kendito.ote.model.GestionOrdenes;
import com.kendito.ote.model.Personal;
import com.kendito.ote.model.Sitio;
import com.kendito.ote.model.SitioG;
import com.kendito.ote.model.valueMensaje;
import com.kendito.ote.model.valueLogin;

import java.io.CharArrayReader;

import retrofit2.Call;
import retrofit2.http.Field;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by Kendo on 20-11-2017.
 */

public interface RegisterAPI {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //consulta para login
    @FormUrlEncoded
    @POST("queryLoginPersonal.php")
    Call<valueLogin> verificarLoginPersonal(@Field("rut") String rut,
                                            @Field("contrasena") String contrasena);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //consultas para ingresar datos
    @FormUrlEncoded
    @POST("queryIngresarArea.php")
    Call<valueMensaje> ingresarArea(@Field("NOMBREAREA") String NOMBREAREA);

    @FormUrlEncoded
    @POST("queryIngresarPersonal.php")
    Call<valueMensaje> ingresarPersonal(@Field("RUTPERSONAL") String RUTPERSONAL,
                                        @Field("IDTIPOPERSONAL") String IDTIPOPERSONAL,
                                        @Field("NOMBRE") String NOMBRE,
                                        @Field("PERSONAL_ASIGNADOR") String PERSONAL_ASIGNADOR,
                                        @Field("CONTRASENA") String CONTRASENA);

    @FormUrlEncoded
    @POST("queryIngresarCliente.php")
    Call<valueMensaje> ingresarCliente(@Field("RUTCLIENTE")String RUTCLIENTE,
                                       @Field("IDTIPOPERSONAL") String IDTIPOPERSONAL,
                                       @Field("IDSITIO") String IDSITIO,
                                       @Field("NOMBRECLIENTE") String NOMBRECLIENTE,
                                       @Field("CONTRASENA") String CONTRASENA);

    @FormUrlEncoded
    @POST("queryIngresarSitio.php")
    Call<valueMensaje> ingresarSitio(@Field("NOMBRESITIO") String NOMBRESITIO,
                                    @Field("IDAREA") String IDAREA);


    @FormUrlEncoded
    @POST("queryIngresarSolicitud.php")
    Call<valueMensaje> ingresarSolicitud(@Field("RUTCLIENTE") String RUTCLIENTE,
                                         @Field("FECHAPEDIDAORDEN") String FECHAPEDIDAORDEN,
                                         @Field("DESCRIPCIONORDEN") String DESCRIPCIONORDEN);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //consultas para ver todos los datos de algo
    @GET("queryViewSitio.php")
    Call<Sitio> verSitios();

    @GET("queryViewCliente.php")
    Call<Cliente> verCliente();

    @GET("queryViewPersonal.php")
    Call<Personal> verPersonal();

    @GET("queryViewArea.php")
    Call<Area> verArea();

    @GET("queryViewSitioG.php")
    Call<SitioG> verSitiosG();

    @GET("queryViewOrdenes.php")
    Call<GestionOrdenes> verGestionOrdenes();

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //consutas para ver datos especificos
    @FormUrlEncoded
    @POST("queryViewSitioE.php")
    Call<SitioG> verSitioE(@Field("NOMBRESITIO") String NOMBRESITIO);

    @FormUrlEncoded
    @POST("queryViewClienteG.php")
    Call<ClienteG> verClienteG(@Field("NOMBRECLIENTE") String NOMBRECLIENTE);

    @FormUrlEncoded
    @POST("queryViewAreaE.php")
    Call<Area> verAreaE(@Field("NOMBREAREA") String NOMBREAREA);

    @FormUrlEncoded
    @POST("queryViewClienteE.php")
    Call<Cliente> verClienteE(@Field("NOMBRECLIENTE")String NOMBRECLIENTE);

    @FormUrlEncoded
    @POST("queryViewPersonalEspecifico.php")
    Call<Personal> verPersonalEspecifico(@Field("RUTPERSONAL") String RUTPERSONAL);


    @FormUrlEncoded
    @POST("queryViewOrdenesEspecialPNA.php")
    Call<GestionOrdenes> verOrdenesE(@Field("RUTPERSONAL") String RUTPERSONAL);

    @FormUrlEncoded
    @POST("queryViewOrdenesEspecialE.php")
    Call<GestionOrdenes> verOrdenesEG(@Field("RUTPERSONAL") String RUTPERSONAL);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //consutas para eliminar datos
    @FormUrlEncoded
    @POST("queryEliminarPersonal.php")
    Call<valueMensaje> eliminarPersonal(@Field("RUTPERSONAL") String RUTPERSONAL);

    @FormUrlEncoded
    @POST("queryEliminarCliente.php")
    Call<valueMensaje> eliminarCliente(@Field("RUTCLIENTE") String RUTCLIENTE);

    @FormUrlEncoded
    @POST("queryEliminarSitio.php")
    Call<valueMensaje> eliminarSitio(@Field("IDSITIO") String IDSITIO);

    @FormUrlEncoded
    @POST("queryEliminarArea.php")
    Call<valueMensaje> eliminarArea(@Field("IDAREA") String IDAREA);

    @FormUrlEncoded
    @POST("queryEliminarOrden.php")
    Call<valueMensaje> eliminarOrden(@Field("CODIGOORDEN") String CODIGOORDEN);

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //consutas para modificar datos
    @FormUrlEncoded
    @POST("queryModificarPersonal.php")
    Call<valueMensaje> modificarPersonal(@Field("RUTPERSONAL") String RUTPERSONAL,
                                         @Field("IDTIPOPERSONAL") String IDTIPOPERSONAL,
                                         @Field("NOMBRE") String NOMBRE,
                                         @Field("PERSONAL_ASIGNADOR") String PERSONAL_ASIGNADOR,
                                         @Field("CONTRASENA") String CONTRASENA);

    @FormUrlEncoded
    @POST("queryModificarCliente.php")
    Call<valueMensaje> modificarCliente(@Field("RUTCLIENTE") String RUTCLIENTE,
                                        @Field("IDTIPOPERSONAL") String IDTIPOPERSONAL,
                                        @Field("IDSITIO") String IDSITIO,
                                        @Field("NOMBRECLIENTE") String NOMBRECLIENTE,
                                        @Field("CONTRASENA") String CONTRASENA);

    @FormUrlEncoded
    @POST("queryModificarAsignarOrden.php")
    Call<valueMensaje> asignarOrden(@Field("RUTPERSONAL") String RUTPERSONAL,
                                    @Field("CODIGOORDEN") String CODIGOORDEN);

    @FormUrlEncoded
    @POST("queryModificarAceptarOrden.php")
    Call<valueMensaje> aceptarOrden(@Field("CODIGOORDEN") String CODIGOORDEN,
                                    @Field("FECHAACEPTADAORDEN") String FECHAACEPTADAORDEN);

    @FormUrlEncoded
    @POST("queryModificarOrdenTerminada.php")
    Call<valueMensaje> terminarOrden(@Field("CODIGOORDEN") String CODIGOORDEN);

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
