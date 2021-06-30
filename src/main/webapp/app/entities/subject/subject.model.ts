import { IAssessment } from 'app/entities/assessment/assessment.model';
import { IDegree } from 'app/entities/degree/degree.model';

export interface ISubject {
  id?: number;
  nameMat?: string | null;
  coefMat?: number | null;
  assessments?: IAssessment[] | null;
  degree?: IDegree;
}

export class Subject implements ISubject {
  constructor(
    public id?: number,
    public nameMat?: string | null,
    public coefMat?: number | null,
    public assessments?: IAssessment[] | null,
    public degree?: IDegree
  ) {}
}

export function getSubjectIdentifier(subject: ISubject): number | undefined {
  return subject.id;
}
