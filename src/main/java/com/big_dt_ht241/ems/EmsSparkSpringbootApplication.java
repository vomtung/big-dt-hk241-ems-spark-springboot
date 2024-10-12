package com.big_dt_ht241.ems;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmsSparkSpringbootApplication {

	public static void main(String[] args) {
		SparkSession spark = SparkSession.builder()
				.appName("Read MySQL Data")
				.master("local")
				.getOrCreate();

		// Cấu hình kết nối JDBC URL, tên bảng, username và password
		String jdbcUrl = "jdbc:mysql://localhost:3306/mydatabase";
		String table = "my_table";
		String user = "your_username";
		String password = "your_password";

		// Đọc dữ liệu từ MySQL bằng JDBC
		Dataset<Row> df = spark.read()
				.format("jdbc")
				.option("url", jdbcUrl)
				.option("dbtable", table)
				.option("user", user)
				.option("password", password)
				.option("driver", "com.mysql.cj.jdbc.Driver")
				.load();

		// Hiển thị dữ liệu
		df.show();

		// Dừng SparkSession sau khi hoàn thành
		spark.stop();
		SpringApplication.run(EmsSparkSpringbootApplication.class, args);
	}

}
