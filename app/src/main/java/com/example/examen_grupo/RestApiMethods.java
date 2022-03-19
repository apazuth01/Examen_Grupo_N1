package com.example.examen_grupo;

public class RestApiMethods {
    private static final String ipaddress = "192.168.43.56";  // FQDN api.dominio.com/
    private static final String StringHttp = "http://";
    // EndPoint Urls
    private static final String GetEmple = "/CRUD-PHP/listaempleados.php";
    private static final String CreateEmple = "/CRUD-PHP/crear.php";
    private static final String ImageUpload = "/CRUD-PHP/UploadFile.php";
    // Metodos CRUD Rest Api para consumo

    // Metodo Get
    public static final String EndPointGetEmple = StringHttp + ipaddress + GetEmple;
    public static final String EndPointCreateEmple = StringHttp + ipaddress + CreateEmple;
    public static final String EndPointImageUpload = StringHttp + ipaddress + ImageUpload;
}
