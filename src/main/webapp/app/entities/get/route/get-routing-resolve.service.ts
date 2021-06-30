import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGet, Get } from '../get.model';
import { GetService } from '../service/get.service';

@Injectable({ providedIn: 'root' })
export class GetRoutingResolveService implements Resolve<IGet> {
  constructor(protected service: GetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((get: HttpResponse<Get>) => {
          if (get.body) {
            return of(get.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Get());
  }
}
