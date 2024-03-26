import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppRoutes } from 'src/app/constants/routes';
import { Hotel } from 'src/app/models/entities/hotel';
import { ErrorResult } from 'src/app/models/results';
import { HotelService } from 'src/app/services/hotel.service';

@Component({
  selector: 'app-hotel-info',
  templateUrl: './hotel-info.component.html',
  styleUrls: ['./hotel-info.component.scss']
})
export class HotelInfoComponent implements OnInit {
  hotel: Hotel | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private hotelService: HotelService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (!Object.keys(params).includes(AppRoutes.HOTEL_ID)) {
        this.returnToHomeScreen();
      }

      const hotelId: number = params[AppRoutes.HOTEL_ID];

      this.hotelService.getById(hotelId).subscribe(result => {
        if (result instanceof ErrorResult) {
          this.returnToHomeScreen();
          return;
        }

        this.hotel = result.getValue();
      });
    });
  }

  returnToHomeScreen(): void {
    this.router.navigateByUrl("");
  }

  placeBookingClicked(): void {
    if (!this.hotel) {
      return;
    }

    this.router.navigateByUrl(AppRoutes.buildRoute(AppRoutes.buildRoute(AppRoutes.BOOKINGS, AppRoutes.ADD)), {
      state: {
        hotelId: this.hotel.id
      }
    });
  }
}
