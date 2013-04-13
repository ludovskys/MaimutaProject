package com.example.maimutaproject1;

public class WebServicesConfig {
	
	private static int LOCAL_LUDO = 1;
	
	private static int PIPIT_LUDO = 2;
	
	private static int PIPIT_ANTOINE = 3;
	
	private static String URL_LOCAL_LUDO = "http://192.168.1.17/sendDataTest/post.php";
	
	private static String URL_PIPIT_LUDO = "https://pipit.u-strasbg.fr/~smoczynski/LPCDED/Maimuta/post.php";
	
	private static String URL_PIPIT_ANTOINE = "https://pipit.u-strasbg.fr/~oblinger/Acrobatt/Interface_client/post.php";

	private int mode;
	private String url;
	
	public WebServicesConfig()
	{
		this.mode = PIPIT_LUDO;
		
		if (mode == LOCAL_LUDO)
		{
			this.url = URL_LOCAL_LUDO;
		}
		else if (mode == PIPIT_LUDO)
		{
			this.url = URL_PIPIT_LUDO;
		}
		else if (mode == PIPIT_ANTOINE)
		{
			this.url = URL_PIPIT_ANTOINE;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
