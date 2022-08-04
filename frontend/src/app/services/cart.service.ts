import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { CartItem } from '../common/cart-item';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  cartItems: CartItem[] = [];

  totalPrice: Subject<number> = new BehaviorSubject<number>(0);
  totalQuantity: Subject<number> = new BehaviorSubject<number>(0);

  storage: Storage = localStorage;

  constructor() {
    let data = JSON.parse(this.storage.getItem('cartItems'));

    if (data != null) {
      this.cartItems = data;
      this.computeCartTotals();
    }
   }

  addToCart(cartItem: CartItem) {
    let alreadyInCart: boolean = false;
    let existingCartItem: CartItem = undefined;

    if (this.cartItems.length > 0) {
      existingCartItem = this.cartItems.find(item => item.id == cartItem.id);
      alreadyInCart = (existingCartItem != undefined);
    }
    if (alreadyInCart) {
      existingCartItem.quantity++;
    }
    else {
      this.cartItems.push(cartItem);
    }
    this.computeCartTotals();
  }

  computeCartTotals() {
    let totalPriceValue: number = 0;
    let totalQuantityValue: number = 0;

    for (let currentItem of this.cartItems) {
      totalPriceValue += currentItem.quantity * currentItem.unitPrice;
      totalQuantityValue += currentItem.quantity;
    }
    // publish the new values
    this.totalPrice.next(totalPriceValue);
    this.totalQuantity.next(totalQuantityValue);

    this.logCartData(totalPriceValue, totalQuantityValue);

    this.persistCartItems();
  }

  persistCartItems() {
    this.storage.setItem('cartItems', JSON.stringify(this.cartItems));
  }

  logCartData(totalPriceValue: number, totalQuantityValue: number) {
    console.log('Contents of the cart:');
    for (let item of this.cartItems) {
      const subTotalPrice = item.quantity * item.unitPrice;
      console.log(`name: ${item.name}, quantity=${item.quantity}, unitPrice=${item.unitPrice}, subTotalPice=${subTotalPrice}`);
    }

    console.log(`totalPrice: ${totalPriceValue.toFixed(2)}, totalQuantity: ${totalQuantityValue}`);
    console.log('---');
  }

  decrementQuantity(cartItem: CartItem) {
    cartItem.quantity--;
    if (cartItem.quantity === 0) {
      this.remove(cartItem);
    }
    else {
      this.computeCartTotals();
    }
  }

  remove(cartItem: CartItem) {
    const itemIdx = this.cartItems.findIndex( item => item.id === cartItem.id );
    if (itemIdx > -1) {
      this.cartItems.splice(itemIdx, 1);
      this.computeCartTotals
    }
  }
}
