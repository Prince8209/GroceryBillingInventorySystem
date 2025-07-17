# Grocery Billing and Inventory System

A Java console application for managing grocery billing and inventory, using JDBC with PostgreSQL.

## Features
- Admin: Register, Login, Add/View/Delete Products
- Customer: Register, Login, View Products, Add to Cart, View Cart, Undo Last Item, Checkout
- Custom data structures: LinkedList (cart), Stack (undo)

## Project Structure
```
GroceryBillingInventorySystem/
│
├── src/
│   ├── ds/
│   │   ├── MyLinkedList.java
│   │   └── MyStack.java
│   ├── CartItem.java
│   ├── DB.java
│   ├── Main.java
│   ├── Product.java
│   └── User.java
│
├── grocery_db.sql
└── README.md
```

## Database Setup
1. Install PostgreSQL and create a database named `grocerydb`.
2. Edit `src/DB.java` and set your DB username and password.
3. Run the SQL script to create tables and insert sample data:
   
   **Windows (CMD):**
   ```cmd
   psql -U your_db_user -d grocerydb -f grocery_db.sql
   ```
   **Linux (bash):**
   ```bash
   psql -U your_db_user -d grocerydb -f grocery_db.sql
   ```

## Compile and Run

**Windows (CMD):**
```cmd
cd src
javac -cp .;postgresql-<version>.jar Main.java
java -cp .;postgresql-<version>.jar Main
```

**Linux (bash):**
```bash
cd src
javac -cp .:postgresql-<version>.jar Main.java
java -cp .:postgresql-<version>.jar Main
```

> Download the PostgreSQL JDBC driver (`postgresql-<version>.jar`) from https://jdbc.postgresql.org/download.html and place it in your project directory.

## Notes
- All methods in custom data structures are public.
- No lambda expressions are used.
- Only console output, no GUI. 