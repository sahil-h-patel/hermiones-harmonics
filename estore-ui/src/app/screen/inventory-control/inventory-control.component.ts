import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../service/product.service';
import { Product } from '../../type';
import { Location } from '@angular/common';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-products',
  templateUrl: './inventory-control.component.html',
  styleUrls: ['./inventory-control.component.scss'],
})
export class InventoryControlComponent implements OnInit {
  products: Product[] = [];

  emptyProduct: Product = {
    id: 0,
    name: '',
    description: '',
    tags: [],
    price: 0,
    quantity: 0,
  };

  // Clone emptyProduct to create a new product as well as the tags list
  newProduct: Product = { ...this.emptyProduct, tags: [] };

  constructor(
    private productService: ProductService,
    private location: Location,
    private _snackBar: MatSnackBar
  ) {
    const token = localStorage.getItem('token');
    if (token == null || !token.startsWith('admin')) {
      console.log(`Unauthorized access: ${token}`);

      this.location.back();
    }
  }

  ngOnInit() {
    this.getProducts();
  }

  updateNewProduct<TAttribute extends keyof Product>(
    productAttribute: TAttribute,
    value: Product[TAttribute]
  ) {
    this.newProduct[productAttribute] = value;
  }

  clearNewProductField() {
    // Clone emptyProduct to create a new product as well as the tags list
    this.newProduct = { ...this.emptyProduct, tags: [] };
  }

  getProducts() {
    this.productService.getProducts().subscribe((products) => {
      // Array is reverted to make the newest product appear first
      this.products = products.reverse();
    });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Close', { duration: 5000 });
  }

  addProduct() {
    this.productService
      .addProduct(this.newProduct)
      .subscribe((product: Product) => {
        this.products.push(product);
        this.getProducts(); // Refreshes product list
        console.log('Product created successfully');
      });
    this.clearNewProductField();
    this.openSnackBar('Product added successfully');
  }

  updateProductTags(product: Product, tags: string[]) {
    product.tags = tags;
    this.updateProduct(product);
  }

  updateProduct(product: Product) {
    this.productService.updateProduct(product).subscribe(() => {
      this.getProducts(); // Refreshes product list
      console.log('Product updated successfully');
    });
  }

  deleteProduct(id: number) {
    this.productService.deleteProduct(id).subscribe(() => {
      this.getProducts(); // Refreshes product list
      console.log('Product deleted successfully');
    });
  }
}
