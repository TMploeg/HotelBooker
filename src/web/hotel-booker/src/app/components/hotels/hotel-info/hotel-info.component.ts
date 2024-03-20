import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppRoutes } from 'src/app/constants/routes';

@Component({
  selector: 'app-hotel-info',
  templateUrl: './hotel-info.component.html',
  styleUrls: ['./hotel-info.component.scss']
})
export class HotelInfoComponent {
  constructor(
    route: ActivatedRoute,
    private router: Router
  ) {
    route.params.subscribe(params => {
      if (!Object.keys(params).includes(AppRoutes.HOTEL_ID)) {
        router.navigateByUrl("");
      }

      console.log('hotelId: ' + params[AppRoutes.HOTEL_ID]);
    });
  }
}
