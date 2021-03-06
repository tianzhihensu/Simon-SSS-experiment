function node_value_info = getRandomNum( node_info, num_monitor )
%GETRANDOMNUM 此处显示有关此函数的摘要
% function node_value_info = getRandomNum( node_info, num_monitor ):
% generate a node list that monitors has been assigned.
% 
% Input:
%       node_info: the initilized nodes list, num_node rows by 4 cols
%       num_monitor: number of monitors
%   
% Output
%       node_value_info: num_node rows by 4 cols
%       node_value_info(:, 1): the value of node
%       node_value_info(:, 2): the MU of node delay
%       node_value_info(:, 3): the SIGMA of node delay
%       node_value_info(:, 4): the normal value of node
node_value_info = node_info;   % initilize the return

% the (num_node + 1) by num_node priority matrix. In this matrix, if the value of element is 0, 
% then a node, whose serial number is the col of the element, won't have a
% monitor equipped.
temp_priority = [
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0   % although the first row is useless, we can use it to decorate the matrix
        14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        14, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        14, 1, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        14, 1, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        14, 1, 2, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        14, 1, 2, 15, 5, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0
        14, 1, 2, 5, 6, 4, 8, 0, 0, 0, 0, 0, 0, 0, 0
        14, 1, 2, 15, 5, 6, 4, 8, 0, 0, 0, 0, 0, 0, 0
        14, 1, 2, 15, 5, 6, 4, 8, 13, 0, 0, 0, 0, 0, 0
        14, 1, 2, 15, 5, 6, 4, 3, 8, 13, 0, 0, 0, 0, 0
        14, 1, 2, 15, 5, 6, 4, 3, 7, 8, 13, 0, 0, 0, 0
        14, 1, 2, 15, 5, 6, 4, 3, 7, 8, 10, 13, 0, 0, 0
        14, 1, 2, 15, 5, 6, 4, 3, 7, 8, 10, 9, 13, 0, 0
        14, 1, 2, 15, 5, 6, 4, 3, 7, 8, 10, 9, 13, 11, 0
        14, 1, 2, 15, 5, 6, 4, 3, 7, 8, 10, 9, 13, 11, 12
];

% prepared for Critical Path--Global Optimal method
cri_path_global_opti = [
    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 0, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 0, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 0, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 9, 0, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 9, 8, 0, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 9, 8, 10, 0, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 9, 8, 10, 13, 0, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 9, 8, 10, 13, 11, 0
    14, 1, 2, 15, 5, 4, 6, 7, 3, 9, 8, 10, 13, 11, 12
];

if num_monitor > 0      % execute for loops while num_monitor is greater than zero
    monitor_row = cri_path_global_opti(num_monitor + 1, :);    % select row from temp_priority by the value of num_monitor
    
    for i=1:num_monitor     % in the case of num_monitor is known, there is no require to check the value of each element is 0 or not.
        index = monitor_row(1, i);
        node_value_info(index, 1) = node_info(index, 4);    % set the selected node to its normal
    end
end
end

