import { Component } from '@angular/core';
import {
  Observable,
  Subject,
  debounceTime,
  distinctUntilChanged,
  switchMap,
} from 'rxjs';
import { Product } from '../../type';
import { ProductService } from '../../service/product.service';

@Component({
  selector: 'app-search-bar',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss'],
})
export class SearchBarComponent {
  products$!: Observable<Product[]>;
  private searchTerms = new Subject<string>();

  constructor(private productService: ProductService) {}

  search(value: string): void {
    this.searchTerms.next(value);
  }

  ngOnInit(): void {
    this.products$ = this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap((term: string) =>
        this.productService.searchProductsByName(term)
      )
    );
  }
}
