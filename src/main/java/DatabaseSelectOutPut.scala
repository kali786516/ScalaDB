/**
 * Created by kalit_000 on 04/12/2015.
 */

import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import org.apache.log4j.Level
//import org.apache.spark._
import java.sql.{ResultSet, DriverManager, Connection}
import java.util.Properties
import org.apache.spark._


object DatabaseSelectOutPut  {

  def main (args: Array[String]):Unit =
  {
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    val conf = new SparkConf().setMaster("local[1]").setAppName("DatabaseSelectExample").set("spark.hadoop.validateOutputSpecs", "false")
    val sc=new SparkContext(conf)

    println("Get the database output")

    val driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"
    val url = "jdbc:sqlserver://localhost;user=admin;password=oracle;database=AdventureWorks2014"
    val username = "admin"
    val password = "oracle"

    var connection: Connection = null

    try {
        Class.forName(driver)

        /*Create connection and statement to run against sql server and execute*/
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("select top 5 CustomerID,StoreID,TerritoryID,AccountNumber from AdventureWorks2014.dbo.Customer")
        resultSet.setFetchSize(10);
        val columnnumber = resultSet.getMetaData().getColumnCount.toInt

        /*OP COLUMN NAMES*/
        var i = 0.toInt;
        for (i <- 1 to columnnumber.toInt)
        {
          val columnname = resultSet.getMetaData().getColumnName(i)
          println("Column Names are:- %s".format(columnname))
        }

        /*OP DATA*/
        while (resultSet.next())
        {
          var list = new java.util.ArrayList[String]()
          for (i <- 1 to columnnumber.toInt) {
            list.add(resultSet.getString(i))

          }
          println(list)
          sc.parallelize(list.toString().replace("null","[null]")).saveAsTextFile("C:\\Users\\kalit_000\\Desktop\\typesafe\\scaladbop\\op.txt")
        }

    } catch {
    case e:Exception => e.printStackTrace
  }
    connection.close()
    //sc.stop()
  }

}
