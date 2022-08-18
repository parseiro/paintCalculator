import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParedeComponent } from '../list/parede.component';
import { ParedeDetailComponent } from '../detail/parede-detail.component';
import { ParedeUpdateComponent } from '../update/parede-update.component';
import { ParedeRoutingResolveService } from './parede-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const paredeRoute: Routes = [
  {
    path: '',
    component: ParedeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParedeDetailComponent,
    resolve: {
      parede: ParedeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParedeUpdateComponent,
    resolve: {
      parede: ParedeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParedeUpdateComponent,
    resolve: {
      parede: ParedeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(paredeRoute)],
  exports: [RouterModule],
})
export class ParedeRoutingModule {}
