package com.li.cart.domain;

import com.li.book.domain.Book;
import com.li.user.domain.User;

import java.math.BigDecimal;

public class CartItem {
    private String cartItemId;
    private int quantity;
    private Book book;
    private User user;


    public String getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(String cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public BigDecimal getSubTotal(){
        BigDecimal b1=new BigDecimal(book.getCurrPrice()+"");
        BigDecimal b2=new BigDecimal(quantity+"");
        return b1.multiply(b2);
    }
}
