import serial #Import Serial Library
import mysql.connector

arduinoSerialData = serial.Serial('com4',9600) #Create Serial port object called arduinoSerialData

while (1):
    if (arduinoSerialData.inWaiting()>0):
        myData = arduinoSerialData.readline()
        x=myData.decode("utf-8")
        t = x.index("_")
        h = x.index("-")
        m = x.index("!")
        temp= x[:t]
        humi = x[(t+1):h]
        moi = x[(h+1):m]
        rain = x[(m+1):]
        print(temp, humi, moi, rain)
        mydb = mysql.connector.connect(
                host="sg2nlmysql3plsk.secureserver.net",
                user="Uttam",
                password="Rahulecs@123",
                database="CropDatabase"
              )
        mycursor = mydb.cursor()
        sql_query = ("INSERT INTO Crop_Prediction(Soil_Moisturizer, Tempreture, Humidity,rainfall)""VALUES (%s, %s, %s,%s)")
                
        values = (moi, temp, humi,rain)

              # Executing query
        mycursor.execute(sql_query, values)
   
        mydb.commit()
        
