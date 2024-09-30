package com.glassis5;

import java.sql.ResultSet;
import java.util.ArrayList;

public class Dao extends Da {

	// 삭제 (1/5) 시작
	public void del(String no) {

		connect();// 고정 1,2,3 connect()또는 super.connect()라고 쓰기
		String sql = String.format("delete from %s where b_no=%s", Db.TABLE_PS_BOARD_FREE, no);
		super.update(sql);// 고정 4
		super.close();// 고정 5
	}

	// 쓰기 (2/5)
	public void write(Dto d) {

		connect();// 고정 1,2,3 connect()또는 super.connect()라고 쓰기
		String sql = String.format(

				"insert into %s (b_title, b_id, b_text) balues ('%s', '%s', '%s')", Db.TABLE_PS_BOARD_FREE, d.title,
				d.id, d.text);

		super.update(sql);// 고정 4
		super.close();// 고정 5

	}

	// 읽기(3/5)
	public Dto read(String no) {

		connect(); // 고정1,2,3 connect() 혹은 super.connect()라고 쓰기
		Dto post = null;
		try {

			String sql = String.format("select * from %s where b_no = %s", Db.TABLE_PS_BOARD_FREE, no);

			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			post = new Dto(

					rs.getString("B_NO"), rs.getString("B_TITLE"), rs.getString("B_ID"), rs.getString("B_DATETIME"),
					rs.getString("B_HIT"), rs.getString("B_TEXT"), rs.getString("B_REPLY_COUNT"),
					rs.getString("B_REPLY_ORI"));
		} catch (Exception e) {

			e.printStackTrace();
		}
		super.close();// 고정 4
		return post;/// 고정 5

	}

	// 리스트(4/5)

	public ArrayList<Dto> list(String page) {

		connect();// 고정 1,2,3 connect()또는 super.connect()라고 쓰기
		ArrayList<Dto> posts = new ArrayList<>();

		try {

			int startIndex = ((Integer.parseInt(page)) - 1) * Board.LIST_AMOUNT;

			String sql = String.format("select * from %s limit %s, %s", Db.TABLE_PS_BOARD_FREE, startIndex,
					Board.LIST_AMOUNT);

			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {

				posts.add(new Dto(

						rs.getString("B_NO"), rs.getString("B_TITLE"), rs.getString("B_ID"), rs.getString("B_DATETIME"),
						rs.getString("B_HIT"), rs.getString("B_TEXT"), rs.getString("B_REPLY_COUNT"),
						rs.getString("B_REPLY_ORI")));
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		super.close();// 고정 4
		return posts;// 고정 5
	}

	// 수정(5/5) 끝

	public void edit(Dto d, String no) {

		connect(); // 고정 1,2,3 connect() 혹은 super.connect()로 작성하기

		String sql = String.format("update %s set b_title = '%s' , b_text = '%s' where b_no=%s", Db.TABLE_PS_BOARD_FREE,
				d.title, d.text, no);
		super.update(sql); // 고정 4
		super.close();// 고정 5
	}

	// 총 글 수 구하기
	public int getPostCount() {

		int count = 0;
		super.connect(); // 고정 1,2,3 super.connect()또는 connect()로 작성하기
		try {

			String sql = String.format("select count(*) from %s", Db.TABLE_PS_BOARD_FREE);
			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(*)");
		} catch (Exception e) {

			e.printStackTrace();
		}
		super.close();
		return count;
	}

	// 총 글 수 구하기(검색)
	public int getSearchPostCount(String word) {

		int count = 0;
		connect(); // 고정 1,2, 3 super.connect()또는 connect()로 작성

		try {

			String sql = String.format("select count (*) from %s where b_title like '%%%s%%'", Db.TABLE_PS_BOARD_FREE,
					word);
			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			rs.next();
			count = rs.getInt("count(*)");
		} catch (Exception e) {

			e.printStackTrace();
		}

		super.close(); // 고정 4
		return count; // 고정 5
	}

	// 글 리스트 (검색)

	public ArrayList<Dto> listSearch(String word, String page) {

		connect(); // 고정 1,2,3 connect()또는 super.connect()로 쓰기
		ArrayList<Dto> posts = new ArrayList<>();

		try {

			int startIndex = ((Integer.parseInt(page)) - 1) * Board.LIST_AMOUNT;
			String sql = String.format("select * from %s where b_title like '%%%s%%' limit %s,%s",
					Db.TABLE_PS_BOARD_FREE, word, startIndex, Board.LIST_AMOUNT);
			System.out.println("sql:" + sql);
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {

				posts.add(new Dto(rs.getString("B_NO"), rs.getString("B_TITLE"), rs.getString("B_ID"),
						rs.getString("B_DATETIME"), rs.getString("B_HIT"), rs.getString("B_TEXT"),
						rs.getString("B_REPLY_COUNT"), rs.getString("B_REPLY_ORI")

				));
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
		super.close();
		return posts;
	}

	// 총 페이지수 구하기
	public int getTotalPageCount() {

		int totalPageCount = 0;
		int count = getPostCount();

		if (count % Board.LIST_AMOUNT == 0) {

			totalPageCount = count / Board.LIST_AMOUNT;
		} else {

			totalPageCount = count / Board.LIST_AMOUNT + 1;
		}
		return totalPageCount;
	}

	// 총 페이지수 구하기 (검색)
	public int getSearchTotalPageCount(String word) {

		int totalPageCount = 0;
		int count = getSearchPostCount(word);

		if (count % Board.LIST_AMOUNT == 0) {

			totalPageCount = count / Board.LIST_AMOUNT;
		} else {

			totalPageCount = count / Board.LIST_AMOUNT + 1;
		}

		return totalPageCount;
	}
}
