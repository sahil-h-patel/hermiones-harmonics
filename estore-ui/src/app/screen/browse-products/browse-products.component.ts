import { Component } from '@angular/core';
import { ProductService } from '../../service/product.service';
import { Product } from '../../type';

@Component({
  selector: 'app-browse-products',
  templateUrl: './browse-products.component.html',
  styleUrls: ['./browse-products.component.scss'],
})
export class BrowseProductsComponent {
  products: Product[] = [];
  numClicks: { [productId: number]: number} = {};

  ascendingOrderAlpha: boolean = true;
  ascendingOrderPrice: boolean = true;
  ascendingOrderPopularity: boolean = true;
  minPrice: number | null = null; // | allows the variable to be a number or null
  maxPrice: number | null = null;


  constructor(private productService: ProductService) {}

  ngOnInit() {
    this.getProducts();
    this.getLocalClickCounts();
  }

  getProducts() {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
    });
  }

  getLocalClickCounts() { // This is used to save the number of clicks even when you click the back button.
    const storedClickCounts = localStorage.getItem('numClicks');
    if (storedClickCounts) {
      this.numClicks = JSON.parse(storedClickCounts);
    }
  }

  incrementNumClicks(product: Product) { // Stores the number of clicks for each link
    console.log("clicked product id: ", product.id);
    if (product.id in this.numClicks) {
      this.numClicks[product.id]++;
    } else {
      this.numClicks[product.id] = 1;
    }
    localStorage.setItem('numClicks', JSON.stringify(this.numClicks)); // Stored in localStorage
  }

  sortAlphabeticallyAscending() {
    this.products.sort((a,b) => a.name.localeCompare(b.name));
    this.ascendingOrderAlpha = true;
  }

  sortAlphabeticallyDescending() {
    this.products.sort((a,b) => b.name.localeCompare(a.name));
    this.ascendingOrderAlpha = false;
  }

  sortPriceAscending() {
    this.products.sort((a,b) => a.price - b.price);
    this.ascendingOrderPrice = true;
  }

  sortPriceDescending() {
    this.products.sort((a,b) => b.price - a.price);
    this.ascendingOrderPrice = false;
  }

  sortPopularityAscending() {
    this.products.sort((a, b) => {
      return (this.numClicks[a.id] || 0) - (this.numClicks[b.id] || 0);
    });
    this.ascendingOrderPopularity = true;
  }
  
  sortPopularityDescending() {
    this.products.sort((a, b) => {
      return (this.numClicks[b.id] || 0) - (this.numClicks[a.id] || 0);
    });
    this.ascendingOrderPopularity = false;
    console.log(this.numClicks);
  }

  filterByPrice() {
    if (this.minPrice === null && this.maxPrice === null) {
      this.getProducts(); // Reset the products list to its original state
      return;
    }
  
    this.products = this.products.filter((product) => {
      if (this.minPrice !== null && this.maxPrice === null) {
        return product.price >= this.minPrice;
      } else if (this.minPrice === null && this.maxPrice !== null) {
        return product.price <= this.maxPrice;
      } else if (this.minPrice !== null && this.maxPrice !== null) {
        return product.price >= this.minPrice && product.price <= this.maxPrice;
      } else {
        return false;
      }
    });

    this.minPrice = null; // Resets input
    this.maxPrice = null;
  }
}
