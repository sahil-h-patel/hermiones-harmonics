import { Component } from '@angular/core';
import { BuyerInfoService } from 'src/app/service/buyerInfo.service';
import { ProductService } from 'src/app/service/product.service';
import { BuyerInfo, Order } from 'src/app/type';
import { Product } from 'src/app/type';
import { DateTime } from 'luxon';
import { Router } from '@angular/router';


@Component({
  selector: 'app-cart-info',
  templateUrl: './cart-info.component.html',
  styleUrls: ['./cart-info.component.scss']
})
export class CartInfoComponent {
  cart : Product[] = [];
  userId : number = 0;
  buyerInfo: BuyerInfo = {} as BuyerInfo;
  buyerId: number = 0;
  totalPrice: number = 0;
  list: Product[] = [];
  constructor(private infoService: BuyerInfoService, 
              private productService: ProductService,
              private router: Router){}

  ngOnInit(): void {
    this.getCart();
  }

  getCart(): void {
    this.infoService.getBuyerInfoByUser().subscribe((data: BuyerInfo) => {
      this.buyerInfo = data;
      this.productService.getProductsByIds(this.buyerInfo.cart).subscribe((data: Product[]) => {
        this.cart = data.filter((product: Product) => product !== null);
        this.getCartTotal();
      });
    });
  }

  delete(index: number): void {
    console.log("Deleting product from cart at index: " + index + " with product id: " + this.cart[index].id);
    this.buyerInfo.cart.splice(index, 1);
    this.infoService.updateBuyerInfo(this.buyerInfo).subscribe();
    this.getCart();
  }

  getCartTotal(): void{
    this.totalPrice = 0;
    this.cart.forEach(product => {
      this.totalPrice+=product.price;
    });
  }

  isCartEmpty(): boolean{
    return this.cart.length == 0;
  }

  purchase(): void {
    this.router.navigateByUrl("/purchase");
  }

}