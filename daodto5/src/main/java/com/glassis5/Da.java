package com.glassis5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Da {

	Connection con = null;
	Statement st = null;

	void connect() {

		try {

			Class.forName(Db.DB_JDBC_DRIVER_PACKAGE_PATH);
			con = DriverManager.getConnection(Db.DB_URL, Db.DB_ID, Db.DB_PW);
			st = con.createStatement();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	void update(String sql) {

		try {

			st.executeUpdate(sql);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	void close() {

		try {

			st.close();
			con.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
