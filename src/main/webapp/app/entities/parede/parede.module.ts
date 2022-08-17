import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParedeComponent } from './list/parede.component';
import { ParedeDetailComponent } from './detail/parede-detail.component';
import { ParedeUpdateComponent } from './update/parede-update.component';
import { ParedeDeleteDialogComponent } from './delete/parede-delete-dialog.component';
import { ParedeRoutingModule } from './route/parede-routing.module';

@NgModule({
  imports: [SharedModule, ParedeRoutingModule],
  declarations: [ParedeComponent, ParedeDetailComponent, ParedeUpdateComponent, ParedeDeleteDialogComponent],
})
export class ParedeModule {}
