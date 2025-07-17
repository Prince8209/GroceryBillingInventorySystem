import java.sql.*;
import java.util.Scanner;
import ds.MyLinkedList;
import ds.MyStack;

class User {

    static void register(Scanner sc) {
        try (Connection conn = DB.getConnection()) {
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();
            System.out.print("Enter role (admin/customer): ");
            String role = sc.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();

            System.out.println("✅ Registration successful!");
        } catch (SQLException e) {
            System.out.println("❌ Registration failed: " + e.getMessage());
        }
    }

    static void login(Scanner sc) {
        try (Connection conn = DB.getConnection()) {
            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("✅ Login successful as " + role);
                if (role.equalsIgnoreCase("admin")) {
                    adminMenu(sc);
                } else {
                    customerMenu(sc);
                }
            } else {
                System.out.println("❌ Invalid credentials.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Login error: " + e.getMessage());
        }
    }

    static void adminMenu(Scanner sc) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Delete Product");
            System.out.println("4. View Stock Report");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                try (Connection conn = DB.getConnection()) {
                    System.out.print("Product name: ");
                    String name = sc.nextLine();
                    System.out.print("Price: ");
                    double price = sc.nextDouble();
                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)");
                    ps.setString(1, name);
                    ps.setDouble(2, price);
                    ps.setInt(3, stock);
                    ps.executeUpdate();
                    System.out.println("✅ Product added.");
                } catch (SQLException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            } else if (choice == 2) {
                try (Connection conn = DB.getConnection()) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM products");
                    while (rs.next()) {
                        Product p = new Product(
                                rs.getInt("product_id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock"));
                        System.out.println(p);
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            } else if (choice == 3) {
                try (Connection conn = DB.getConnection()) {
                    System.out.print("Enter product ID to delete: ");
                    int id = sc.nextInt();
                    PreparedStatement ps = conn.prepareStatement(
                            "DELETE FROM products WHERE product_id=?");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    System.out.println("✅ Product deleted.");
                } catch (SQLException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            } else if (choice == 4) {
                try (Connection conn = DB.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE stock <= 5");
                    ResultSet rs = ps.executeQuery();
                    System.out.println("\n📦 Low Stock Report (<= 5):");
                    while (rs.next()) {
                        Product p = new Product(
                                rs.getInt("product_id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock"));
                        System.out.println(p);
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            } else if (choice == 5) {
                break;
            }
        }
    }

    static void customerMenu(Scanner sc) {
        MyLinkedList<CartItem> cart = new MyLinkedList<>();
        MyStack<CartItem> undoStack = new MyStack<>();

        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. Undo Last Item");
            System.out.println("4. View Cart");
            System.out.println("5. Checkout & Save Bill");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                try (Connection conn = DB.getConnection()) {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM products");
                    while (rs.next()) {
                        Product p = new Product(
                                rs.getInt("product_id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock"));
                        System.out.println(p);
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            } else if (choice == 2) {
                try (Connection conn = DB.getConnection()) {
                    System.out.print("Enter Product ID: ");
                    int id = sc.nextInt();
                    System.out.print("Enter quantity: ");
                    int qty = sc.nextInt();

                    PreparedStatement ps = conn.prepareStatement("SELECT * FROM products WHERE product_id=?");
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        Product p = new Product(
                                rs.getInt("product_id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock")
                        );
                        if (qty > p.stock) {
                            System.out.println("❌ Not enough stock!");
                        } else {
                            CartItem item = new CartItem(p, qty);
                            cart.add(item);
                            undoStack.push(item);
                            System.out.println("✅ Item added to cart.");
                        }
                    } else {
                        System.out.println("❌ Product not found.");
                    }
                } catch (SQLException e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }
            } else if (choice == 3) {
                CartItem removed = undoStack.pop();
                if (removed != null) {
                    for (int i = 0; i < cart.size(); i++) {
                        CartItem item = cart.getAt(i);
                        if (item.product.id == removed.product.id && item.quantity == removed.quantity) {
                            cart.removeAt(i);
                            break;
                        }
                    }
                    System.out.println("✅ Last item removed.");
                } else {
                    System.out.println("❌ Nothing to undo.");
                }
            } else if (choice == 4) {
                for (int i = 0; i < cart.size(); i++) {
                    CartItem item = cart.getAt(i);
                    System.out.println(item);
                }
            } else if (choice == 5) {
                double total = 0;
                StringBuilder bill = new StringBuilder();

                // Generate formatted bill
                bill.append("🧾 --- Billing Invoice ---\n");
                bill.append(String.format("%-20s %5s %10s\n", "Item", "Qty", "Amount"));
                bill.append("------------------------------------------\n");

                for (int i = 0; i < cart.size(); i++) {
                    CartItem item = cart.getAt(i);
                    double itemTotal = item.getTotal();
                    total += itemTotal;
                    bill.append(String.format("%-20s %5d %10.2f\n",
                            item.product.name, item.quantity, itemTotal));
                }

                bill.append("------------------------------------------\n");
                bill.append(String.format("Total Amount: Rs. %.2f\n", total));
                bill.append("🧾 Thank you for shopping!\n");

                // Print to console
                System.out.println(bill.toString());

                // Save to .txt file
                try {
                    String timestamp = java.time.LocalDateTime.now()
                            .toString().replace(":", "").replace(".", "").replace("T", "_");
                    String filename = "invoice_" + timestamp + ".txt";

                    java.io.FileWriter fw = new java.io.FileWriter(filename);
                    fw.write(bill.toString());
                    fw.close();

                    System.out.println("📁 Invoice saved as: " + filename);
                } catch (java.io.IOException e) {
                    System.out.println("❌ Error writing file: " + e.getMessage());
                }

                // Clear cart and undo stack
                cart.clear();
                undoStack = new MyStack<>();
            }

            else if (choice == 6) {
                break;
            }
        }
    }
}
