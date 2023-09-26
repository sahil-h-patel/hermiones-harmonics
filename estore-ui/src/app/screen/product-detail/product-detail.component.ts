import { Component, Input } from '@angular/core';
import { BuyerInfo, Cart, Product } from '../../type';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../service/product.service';
import { Location } from '@angular/common';
import { BuyerInfoService } from 'src/app/service/buyerInfo.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent {
  @Input() product: Product = {} as Product;
  cart: number[] = [];
  buyerInfo: BuyerInfo= {} as BuyerInfo;
  buyerInfoCopy: BuyerInfo = {} as BuyerInfo;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private productService: ProductService,
    private buyerService: BuyerInfoService,
    private location: Location,
    private _snackBar: MatSnackBar,
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.getProduct(params['id']);
    });
  }

  getProduct(id: number): void {
    this.productService.getProduct(id).subscribe((product) => {
      this.product = product;
    });
  }

  goBack(): void {
    this.location.back();
  }

  addtoCart(): void {
    this.buyerService.getBuyerInfoByUser().subscribe((buyerInfo: BuyerInfo) => {
      this.buyerInfo = buyerInfo;
      console.log(buyerInfo.id);

      this.buyerService.getBuyerCart().subscribe((cartData: any) => {
        this.cart = cartData;

        if (!this.cart || !this.product) {
          this.router.navigateByUrl('/signup');
          return;
        }

        this.cart.push(this.product.id);

        this.buyerInfoCopy = {...this.buyerInfo};
        this.buyerInfoCopy.cart = [...this.cart];
        
        this.buyerService.updateBuyerInfo(this.buyerInfoCopy).subscribe((updatedBuyerInfo: BuyerInfo) => {
          this.buyerInfo = updatedBuyerInfo;
          console.log('Product added to cart:', this.product);
        });
        this._snackBar.open('Product added to cart', 'Close', {
          duration: 2000,
        });
      }
      );
    });
  }

  isAdminPage(): boolean {
    return localStorage.getItem('token')?.startsWith('admin') ?? false;
  }
}
