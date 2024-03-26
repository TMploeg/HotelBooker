import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { delay } from 'rxjs';
import { AppRoutes } from 'src/app/constants/routes';
import { Hotel } from 'src/app/models/entities/hotel';
import { ErrorResult, SuccesResult } from 'src/app/models/results';
import { HotelService } from 'src/app/services/hotel.service';

@Component({
  selector: 'app-hotel-list',
  templateUrl: './hotel-list.component.html',
  styleUrls: ['./hotel-list.component.scss']
})
export class HotelListComponent {
  hotels?: Hotel[];
  loading: boolean = false;

  constructor(
    private router: Router,
    private hotelService: HotelService,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.loading = true;
    this.hotelService.getAll().subscribe(result => {
      if (result instanceof SuccesResult) {
        this.hotels = result.getValue();
      }
      else {
        this.snackbar.open("ERROR: failed to load hotels", "ok", {
          duration: 3000
        });
      }

      this.loading = false;
    });
  }

  navigate(hotel: Hotel) {
    this.router.navigateByUrl(AppRoutes.buildRoute(AppRoutes.HOTELS, hotel.id.toString()));
  }
}
