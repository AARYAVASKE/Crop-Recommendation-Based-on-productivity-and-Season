##import libraries
import pandas as pd
import numpy as np
import mysql.connector
from sklearn.model_selection import train_test_split
## selection
data=pd.read_csv('cpdata.csv')
print(data.head(1))
## display data
label= pd.get_dummies(data.label).iloc[: , 1:]
data= pd.concat([data,label],axis=1)
data.drop('label', axis=1,inplace=True)
## printing of data
print('The data present in one row of the dataset is')
print(data.head(1))
train=data.iloc[:, 0:4].values
test=data.iloc[: ,4:].values
X_train,X_test,y_train,y_test=train_test_split(train,test,test_size=0.3)
from sklearn.preprocessing import StandardScaler
sc = StandardScaler()
X_train = sc.fit_transform(X_train)
X_test = sc.transform(X_test)
## decision tree classification
from sklearn.tree import DecisionTreeRegressor
clf=DecisionTreeRegressor()

#Fitting the classifier into training set
clf.fit(X_train,y_train)
pred=clf.predict(X_test)
## using sklearn library 
from sklearn.metrics import accuracy_score
# Finding the accuracy of the model
#a=accuracy_score(y_test,pred)
#print("The accuracy of this model is: ", a*100)

#establishing the connection
conn = mysql.connector.connect(
   user='Uttam', password='Rahulecs@123', host='sg2nlmysql3plsk.secureserver.net', database='CropDatabase')

#Creating a cursor object using the cursor() method
cursor = conn.cursor()

#Retrieving single row
sql = '''SELECT * FROM Crop_Prediction ORDER BY ID DESC LIMIT 1'''

#Executing the query
cursor.execute(sql)

#Fetching 1st row from the table
result = cursor.fetchone()
print(result)

#Fetching 1st row from the table


# assign the values to variables
var1, var2, var3,var4= result[1], result[2], result[3], result[4]
print(var1)
print(var2)
print(var3)
print(var4)
# ah=14
# atemp=20
# ph=32
# rain=0

l=[]
l.append(var1)
l.append(var2)
l.append(var3)
l.append(var4)
predictcrop=[l]

# Putting the names of crop in a single list
crops=['wheat','mungbean','Tea','millet','maize','lentil','jute','cofee','cotton','ground nut','peas','rubber','sugarcane','tobacco','kidney beans','moth beans','coconut','blackgram','adzuki beans','pigeon peas','chick peas','banana','grapes','apple','mango','muskmelon','orange','papaya','watermelon','pomegranate']
cr='';
## prediction is done here
predictions = clf.predict(predictcrop)
count=0
global valpredict

for i in range(0,30):
    if(predictions[0][i]==1):
        c=crops[i]
        count=count+1
        break;
    if(predictions[0][i]==0):
        cr=crops[i]
        count=count+1
        
    i=i+1

print('The predicted crop is %s'%cr)
print('The predicted crop is %s'%c)
valpredict=c+"-"+cr
cursor.execute('SELECT MAX(ID) FROM Predict_Table')

# Fetch the result and assign it to a variable
max_id = cursor.fetchone()[0]
crop=valpredict
print(crop)
cursor = conn.cursor()

mycursor = conn.cursor()
sql_query = ("INSERT INTO Predict_Table(ID, Prediction)""VALUES (%s, %s)")
                
values = (max_id+1, crop)

              # Executing query
mycursor.execute(sql_query, values)
   
conn.commit()
 



