import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { AppRoutes } from 'src/app/constants/routes';
import { Hotel } from 'src/app/models/entities/hotel';
import { HotelService } from 'src/app/services/hotel.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

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

      this.hotelService.getById(hotelId).subscribe(hotel => {
        if (hotel == null) {
          this.returnToHomeScreen();
        }

        this.hotel = hotel;
      });
    });
  }

  returnToHomeScreen(): void {
    this.router.navigateByUrl("");
  }
}
