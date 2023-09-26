import {
  ChangeDetectionStrategy,
  Component,
  Input,
  OnChanges,
} from '@angular/core';
import { ProductService } from 'src/app/service/product.service';
import { Order, Product } from 'src/app/type';

import { DateTime } from 'luxon';
import { OrderService } from 'src/app/service/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  changeDetection: ChangeDetectionStrategy.Default,
})
export default class OrderComponent implements OnChanges {
  @Input() order?: Order;
  dateFormatted: string = '';
  total = 0;
  products: Product[] = [];
  selectedOrderStatus: 'UNPROCESSED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' =
    'UNPROCESSED';

  constructor(
    private productService: ProductService,
    private orderService: OrderService
  ) {}

  ngOnChanges(): void {
    if (this.order === undefined) {
      return;
    }

    this.dateFormatted = DateTime.fromMillis(this.order.date).toLocaleString(
      DateTime.DATE_SHORT
    );
    this.selectedOrderStatus = this.order.orderStatus;

    this.productService
      .getProductsByIds(this.order.productId)
      .subscribe((products) => {
        this.products = products.filter((product: any) => product !== null);
        this.total = this.products.reduce(
          (acc, product) => acc + product.price,
          0
        );
      });
  }

  trackProductItem(index: number, product: Product): number {
    return product.id;
  }

  isAdminPage(): boolean {
    return localStorage.getItem('token')?.startsWith('admin') ?? false;
  }

  updateOrderStatus(): void {
    if (this.order === undefined) {
      return;
    }
    this.order.orderStatus = this.selectedOrderStatus;
    this.orderService.updateOrder(this.order).subscribe((order) => {
      console.log('updated order: ' + order.orderStatus);
    });
  }
}
