import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDegree, Degree } from '../degree.model';
import { DegreeService } from '../service/degree.service';

@Injectable({ providedIn: 'root' })
export class DegreeRoutingResolveService implements Resolve<IDegree> {
  constructor(protected service: DegreeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDegree> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((degree: HttpResponse<Degree>) => {
          if (degree.body) {
            return of(degree.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Degree());
  }
}
