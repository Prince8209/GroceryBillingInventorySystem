class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getTotal() {
        return product.price * quantity;
    }

    @Override
    public String toString() {
        return product.name + " x " + quantity + " = Rs." + getTotal();
    }
}
