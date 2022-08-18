import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'sala',
        data: { pageTitle: 'Salas' },
        loadChildren: () => import('./sala/sala.module').then(m => m.SalaModule),
      },
      {
        path: 'parede',
        data: { pageTitle: 'Paredes' },
        loadChildren: () => import('./parede/parede.module').then(m => m.ParedeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
