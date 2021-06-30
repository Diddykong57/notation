import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GetComponent } from './list/get.component';
import { GetDetailComponent } from './detail/get-detail.component';
import { GetUpdateComponent } from './update/get-update.component';
import { GetDeleteDialogComponent } from './delete/get-delete-dialog.component';
import { GetRoutingModule } from './route/get-routing.module';

@NgModule({
  imports: [SharedModule, GetRoutingModule],
  declarations: [GetComponent, GetDetailComponent, GetUpdateComponent, GetDeleteDialogComponent],
  entryComponents: [GetDeleteDialogComponent],
})
export class GetModule {}
