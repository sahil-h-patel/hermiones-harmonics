import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BuyerInfoService } from 'src/app/service/buyerInfo.service';
import { OrderService } from 'src/app/service/order.service';
import { ProductService } from 'src/app/service/product.service';
import { BuyerInfo, Product, Order, CreditCard} from 'src/app/type';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.scss']
})
export class PurchaseComponent {
  buyerInfo: BuyerInfo = {} as BuyerInfo;
  cart: Product[] = [];
  totalPrice: number = 0;
  selectedAddress: string = "";
  selectedCreditCard: CreditCard = {} as CreditCard;

  
  constructor(private buyerService: BuyerInfoService, private productService: ProductService,
    private _snackBar: MatSnackBar, private orderService: OrderService, private router: Router, ) { 
    this.getInfo();
  }
  
  getInfo(){
    this.buyerService.getBuyerInfoByUser().subscribe((data: BuyerInfo) => {
      this.buyerInfo = data;
      this.productService.getProductsByIds(this.buyerInfo.cart).subscribe((data: Product[]) => {
        this.cart = data.filter((product: Product) => product != null);
        if (this.cart.length != this.buyerInfo.cart.length) {
          // missing product will be deleted off of cart
          this.buyerInfo.cart = this.cart.map((product: Product) => product.id);
          this.buyerService.updateBuyerInfo(this.buyerInfo);
        }
        this.cart = this.cart.filter((product: Product) => product.quantity > 0);
        this.totalPrice = this.cart.reduce((total: number, product: Product) => total + product.price, 0);
      });
      if (this.buyerInfo.shippingAddresses.length == 0 || this.buyerInfo.creditCards.length == 0){
        this._snackBar.open("Please add a shipping address or credit card to your account");
      }
    });
  }

  // To purchase:
  // 1) Create Order object from items in cart
  // 2) Modify buyer's past order ids with these object's ids
  // 3) Modify product quantities
  // 4) Clear buyer's cart
  purchase(): void {
    if(this.buyerInfo.shippingAddresses.length == 0 || this.buyerInfo.creditCards.length == 0) {
      this._snackBar.open("Please add a shipping address or credit card to your account");
      return;
    }
    
    if (!this.selectedCreditCard || !this.selectedAddress) {
      console.log(this.selectedCreditCard, this.selectedAddress);
      
      this._snackBar.open("Please select a credit card and shipping address");
      return;
    }

    const newOrder: Order = {
      productId: this.buyerInfo.cart,
      date: new Date().getTime(),
      orderStatus: "UNPROCESSED",
      address: this.selectedAddress,
      ccDigits: this.getLastFourDigitsCard(this.selectedCreditCard),
      orderID: -1, // Temporary, will be overwritten
      userID: this.buyerInfo.userId,
    }

    // Order gets created and product quantities get decremented (in backend)
    this.orderService.createOrder(newOrder).subscribe((data: Order) => {
      this.buyerInfo.pastOrdersIds.push(data.orderID);
      this.buyerService.updateBuyerInfo(this.buyerInfo);

      // Clear buyer's cart
      this.buyerInfo.cart = [];
      this.buyerService.updateBuyerInfo(this.buyerInfo).subscribe(() => {
        // After cart is cleared, redirect to past orders
        this.router.navigateByUrl("/orders");
        this._snackBar.open("Order placed!", "Close",{duration: 3000});
      });
    });
  }

  getLastFourDigitsCard(creditCard: CreditCard): number {
    const cardNumber = creditCard.cardNumber;
    return parseInt(cardNumber.substring(cardNumber.length - 4));
  }
}
