function res_time_first = getFirstResTime( first_node_info )
%GETFIRSTRESTIME �˴���ʾ�йش˺�����ժҪ
% function res_time_first = getFirstResTime( first_node_info ): generate
% total response time of first phase.

% Input:
%       first_node_info: list of nodes/edges that make up the first phase.
% Output:
%       res_time_first: total response time of first phase.

res_time_first = 0;
node_value = first_node_info(:, 1);
res_time_first = sum(node_value);

end

