package com.kali.db

/**
 * Created by kalit_000 on 04/12/2015.
 */

import org.apache.spark.SparkConf
import org.apache.log4j.Logger
import org.apache.log4j.Level
import java.sql._
import org.apache.spark._
import org.springframework.jdbc.core.simple._
import org.springframework.context.support.ClassPathXmlApplicationContext

case class CustomerTable(CustomerID:String,PersonID:String)

class SpringTestDB extends SimpleJdbcDaoSupport {

  def getCountOfrows(query:String):Any = {
    return getJdbcTemplate.queryForInt(query)
  }

}

class sqlqueryclass (querystring:String) {
  def queryvalue(): String = {
    querystring
  }
}

object DBSelectSping extends App{

    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    val conf = new SparkConf().setMaster("local[2]").setAppName("DatabaseSelectExample")
    val sc=new SparkContext(conf)

    //read the application context file
    val ctx=new ClassPathXmlApplicationContext("applicationContext.xml")

    // get a SpringTestDB instance
    val executequery=ctx.getBean("springtest").asInstanceOf[SpringTestDB]
    val sqlquery=ctx.getBean("sqlquerytest").asInstanceOf[sqlqueryclass]


    val results=executequery.getCountOfrows(sqlquery.queryvalue)

    println("No of rows in customer table:-%s".format(results))

}
