package Test;

import java.sql.*;
import java.util.Scanner;

class Data {
    String name;
    String phoneNumber;
    String address;

    public String getName() {
        return name;
}

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

class SQLC {
    private static Connection con;
    private static PreparedStatement pstmt;

    SQLC() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/phone"
                , "root", "1234");
    }

    void Insert(Data d) {
        try {
            pstmt = con.prepareStatement("insert into phone values (?, ?, ?);");
            pstmt.setString(1, d.getName());
            pstmt.setString(2, d.getPhoneNumber());
            pstmt.setString(3, d.getAddress());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    void selectAll() throws SQLException {
        String str = "select * from phone;";
        pstmt = con.prepareStatement(str);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            System.out.print("이름:");
            System.out.print(rs.getString("name") + "/");
            System.out.print("전화번호:");
            System.out.print(rs.getString("phoneNumber") + "/");
            System.out.print("주소:");
            System.out.print(rs.getString("address"));
            System.out.println();
        }
    }

    void search(String name) throws SQLException {
        String str = "select * from phone where name = ?;";
        pstmt = con.prepareStatement(str);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            System.out.print("이름:");
            System.out.print(rs.getString("name") + "/");
            System.out.print("전화번호:");
            System.out.print(rs.getString("phoneNumber") + "/");
            System.out.print("주소:");
            System.out.print(rs.getString("address"));
            System.out.println();
        } else {
            System.out.println("전화번호부에 없습니다");
        }
    }

    void delete(String name) throws SQLException {
        String str = "delete from phone where name = ?;";
        pstmt = con.prepareStatement(str);
        pstmt.setString(1, name);
        if (pstmt.executeUpdate() == 0) {
            System.out.println("전화번호부에 없습니다");
        } else {
            System.out.println("삭제 완료");
        }


    }

}

class Input {

    Data inputValues() {
        Scanner sc = new Scanner(System.in);
        Data d = new Data();
        System.out.print("이름:");
        d.setName(sc.nextLine());
        System.out.print("전화번호:");
        d.setPhoneNumber(sc.nextLine());
        System.out.print("주소:");
        d.setAddress(sc.nextLine());

        return d;
    }

    String searchName() {
        Scanner sc = new Scanner(System.in);
        System.out.print("이름:");
        return sc.nextLine();
    }
}

class PhoneBook {

    void start(SQLC sqlc, Scanner sc, Input input) throws SQLException {
        while (true) {
            System.out.print("1.입력 2.검색 3.삭제 4.출력 5.종료:");

            int num = sc.nextInt();
            if (num == 1) {
                sqlc.Insert(input.inputValues());
            } else if (num == 2) {
                sqlc.search(input.searchName());

            } else if (num == 3) {
                sqlc.delete(input.searchName());

            } else if (num == 4) {
                sqlc.selectAll();

            } else if (num == 5) {
                System.out.println("프로그램을 종료합니다.");
                break;

            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }
}

public class Phone {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        SQLC sqlc = new SQLC();
        Input input = new Input();
        PhoneBook pb = new PhoneBook();

        pb.start(sqlc, sc, input);


    }
}


