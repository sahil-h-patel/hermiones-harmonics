import { Component } from '@angular/core';
import { OrderService } from 'src/app/service/order.service';
import { Order } from 'src/app/type';
import { Location } from '@angular/common';

@Component({
  selector: 'app-display-orders',
  templateUrl: './display-orders.component.html',
  styleUrls: ['./display-orders.component.scss']
})
export class DisplayOrdersComponent {
  orders: Order[] = [];
  order: any;
  token: any;
  constructor(
    private orderService: OrderService, 
    private location: Location
  ){
    this.token = localStorage.getItem('token');
    if (this.token == null) {
      this.location.back();
    }
  }

  ngOnInit(){

    if(this.token == 'admin:admin') {
      this.getOrders();
    } else {
      this.getPastOrders();
    }
    
  }

  getOrders() : void{
    this.orderService.getAllOrders().subscribe((allOrders) => {
      this.orders = allOrders;
      console.log("Retrieved all orders: " + this.token)
    })
  }

  getPastOrders() : void{
    this.orderService.getOrdersofUser().subscribe((pastOrders) => {
      this.orders = pastOrders;
      console.log("Retrieved all past orders");
    })
  }

  isAdmin(): boolean {
    return this.token == 'admin:admin';
  }
}
