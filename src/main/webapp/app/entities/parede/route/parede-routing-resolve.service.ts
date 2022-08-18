import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParede } from '../parede.model';
import { ParedeService } from '../service/parede.service';

@Injectable({ providedIn: 'root' })
export class ParedeRoutingResolveService implements Resolve<IParede | null> {
  constructor(protected service: ParedeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParede | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parede: HttpResponse<IParede>) => {
          if (parede.body) {
            return of(parede.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
