package mypackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class myservlet
 */
public class myservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public myservlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//API Key
				String apiKey = "a04f6528e60259abebc135924fe8ac3e";
				// Get the city from the form input
		        String city = request.getParameter("city"); 

		        // Create the URL for the OpenWeatherMap API request
		        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
		        URL url=new URL(apiUrl);
		        //api integration
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setRequestMethod("GET");
		        //read data from network
		        InputStream inputstream = connection.getInputStream();
		        InputStreamReader reader = new InputStreamReader(inputstream);
                //wants to store in a string
		        StringBuilder responsecontent = new StringBuilder();
		        //input ke liye reader create scanner object
		        Scanner sc = new Scanner(reader);
		        
		        while(sc.hasNext())
		        {
		        	responsecontent.append(sc.nextLine());
		    	}
		     sc.close();
		     //parse the JsON to extract the features i.e temp,humidity
		     Gson gson = new Gson();
		     JsonObject jsonobject = gson.fromJson(responsecontent.toString(),JsonObject.class);
		     
		     //date and time
		     long dateTimeStamp = jsonobject.get("dt").getAsLong()*1000;
		     String date= new Date(dateTimeStamp).toString();
		     
		     //temperature
		     double temperatureKelvin = jsonobject.getAsJsonObject("main").get("temp").getAsDouble();
             int temperatureCelsius = (int) (temperatureKelvin - 273.15);
		    
           //Humidity
             int humidity = jsonobject.getAsJsonObject("main").get("humidity").getAsInt();
             
             //Wind Speed
             double windSpeed = jsonobject.getAsJsonObject("wind").get("speed").getAsDouble();
             
           //Weather Condition
             String weatherCondition = jsonobject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").toString();
             
             // Set the data as request attributes (for sending to the jsp page)
             request.setAttribute("date", date);
             request.setAttribute("city", city);
             request.setAttribute("temperature", temperatureCelsius);
             request.setAttribute("weatherCondition", weatherCondition); 
             request.setAttribute("humidity", humidity);    
             request.setAttribute("windSpeed", windSpeed);
             request.setAttribute("weatherData", responsecontent.toString());
             
             connection.disconnect();
             
             // Forward the request to the weather.jsp page for rendering
             request.getRequestDispatcher("index.jsp").forward(request, response);
             
	}

}
